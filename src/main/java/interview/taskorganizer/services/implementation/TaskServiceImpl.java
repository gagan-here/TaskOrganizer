package interview.taskorganizer.services.implementation;

import interview.taskorganizer.dto.CommentDto;
import interview.taskorganizer.dto.TaskDto;
import interview.taskorganizer.dto.TaskStatusDto;
import interview.taskorganizer.entities.TaskEntity;
import interview.taskorganizer.exceptions.BusinessLogicException;
import interview.taskorganizer.exceptions.ResourceNotFoundException;
import interview.taskorganizer.mapper.CommentMapper;
import interview.taskorganizer.mapper.TaskMapper;
import interview.taskorganizer.mapper.TaskStatusMapper;
import interview.taskorganizer.repositories.CommentRepository;
import interview.taskorganizer.repositories.TaskRepository;
import interview.taskorganizer.repositories.TaskStatusRepository;
import interview.taskorganizer.services.TaskService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

  // Repositories
  private final TaskRepository taskRepository;
  private final CommentRepository commentRepository;
  private final TaskStatusRepository taskStatusRepository;

  // Mappers
  private final TaskMapper taskMapper;
  private final CommentMapper commentMapper;
  private final TaskStatusMapper taskStatusMapper;

  @Override
  public TaskDto createTask(TaskDto taskDto) {
    String username = getUsername();
    log.info("User {} is creating a task with title: {}", username, taskDto.getTitle());

    TaskEntity toBeSaved = taskMapper.toEntity(taskDto);
    toBeSaved.setAssignee(username);
    taskRepository.save(toBeSaved);

    log.info("Task created");
    return taskMapper.toDTO(toBeSaved);
  }

  @Override
  public TaskDto getTaskById(Long id) {
    log.info("Retrieving task with id: {}", id);
    final var existingEntity =
        taskRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    return taskMapper.toDTO(existingEntity);
  }

  @Override
  public List<TaskDto> getTasksForUser() {
    final var username = getUsername();
    log.info("Retrieving tasks for user: {}", username);
    return taskRepository.findAllByAssignee(username).stream().map(taskMapper::toDTO).toList();
  }

  @Override
  public TaskDto updateTask(Long id, TaskDto taskDto) {
    log.info("Updating task with Id: {}", id);
    final var existingEntity =
        taskRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Task with ID {} not found for update", id);
                  return new ResourceNotFoundException("Task not found");
                });

    if (!Objects.equals(existingEntity.getAssignee(), getUsername())) {
      log.error("User: {} is not authorized to update task", getUsername());
      throw new BusinessLogicException("403", "Task cannot be updated.");
    }

    existingEntity.setTitle(taskDto.getTitle());
    existingEntity.setDescription(taskDto.getDescription());
    existingEntity.setDueDate(taskDto.getDueDate());

    final var updatedEntity = taskRepository.save(existingEntity);
    log.info("Task updated successfully");
    return taskMapper.toDTO(updatedEntity);
  }

  @Override
  public void changeStatus(Long id, String status) {
    TaskEntity task =
        taskRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Task with id {} not found for status change", id);
                  return new ResourceNotFoundException("Task not found");
                });
    log.info("Changing status of task with id: {} to {}", id, status);
    final var savedEntity =
        taskStatusMapper.toEntity(new TaskStatusDto(status.toUpperCase()), task);
    taskStatusRepository.save(savedEntity);
    log.info("Status changed to {} for task id {}", id, status);
  }

  @Override
  public void addCommentToTask(Long id, CommentDto commentDto) {
    final var existingEntity =
        taskRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Task with id {} not found for adding comment", id);
                  return new ResourceNotFoundException("Task not found");
                });

    log.info("Adding comment to task with id: {}", id);
    commentDto.setReviewer(getUsername());

    final var savedEntity = commentMapper.toEntity(commentDto, existingEntity);
    commentRepository.saveAndFlush(savedEntity);
    log.info("Comment added to task with id: {}", id);
  }

  @Override
  public void deleteTask(Long id) {
    log.info("Deleting task with ID: {}", id);
    final var task =
        taskRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("Task with ID {} not found for deletion", id);
                  return new ResourceNotFoundException("Task not found");
                });
    taskRepository.delete(task);
    log.info("Task with ID {} deleted successfully", id);
  }

  private static String getUsername() {
    return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .getUsername();
  }
}

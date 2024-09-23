package interview.taskorganizer.services;

import interview.taskorganizer.dto.CommentDto;
import interview.taskorganizer.dto.TaskDto;
import java.util.List;

public interface TaskService {
  TaskDto createTask(TaskDto taskDto);

  TaskDto getTaskById(Long id);

  List<TaskDto> getTasksForUser();

  TaskDto updateTask(Long id, TaskDto taskDto);

  void changeStatus(Long id, String status);

  void addCommentToTask(Long id, CommentDto commentDto);

  void deleteTask(Long id);
}

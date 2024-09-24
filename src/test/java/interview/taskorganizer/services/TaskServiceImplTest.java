package interview.taskorganizer.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import interview.taskorganizer.dto.CommentDto;
import interview.taskorganizer.dto.TaskDto;
import interview.taskorganizer.dto.TaskStatusDto;
import interview.taskorganizer.entities.CommentEntity;
import interview.taskorganizer.entities.TaskEntity;
import interview.taskorganizer.entities.TaskStatusEntity;
import interview.taskorganizer.mapper.CommentMapper;
import interview.taskorganizer.mapper.TaskMapper;
import interview.taskorganizer.mapper.TaskStatusMapper;
import interview.taskorganizer.repositories.CommentRepository;
import interview.taskorganizer.repositories.TaskRepository;
import interview.taskorganizer.repositories.TaskStatusRepository;
import interview.taskorganizer.services.implementation.TaskServiceImpl;
import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

class TaskServiceImplTest {

  @InjectMocks private TaskServiceImpl taskService;

  @Mock private TaskRepository taskRepository;

  @Mock private CommentRepository commentRepository;

  @Mock private TaskStatusRepository taskStatusRepository;

  @Mock private TaskMapper taskMapper;

  @Mock private CommentMapper commentMapper;

  @Mock private TaskStatusMapper taskStatusMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // mock chain of calls
    final var mockSecurityContext = mock(SecurityContext.class);
    final var mockAuthentication = mock(Authentication.class);
    final var mockUserDetails = mock(UserDetails.class);

    // mock responses for each method calls
    when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
    when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
    when(mockUserDetails.getUsername()).thenReturn("testUser");

    SecurityContextHolder.setContext(mockSecurityContext);
  }

  @Test
  void testCreateTask() {
    // create fake dto and entity
    TaskDto mockTaskDto = Instancio.create(TaskDto.class);
    TaskEntity mockTaskEntity = Instancio.create(TaskEntity.class);

    // mock return value of method calls
    when(taskMapper.toEntity(any(TaskDto.class))).thenReturn(mockTaskEntity);
    when(taskMapper.toDTO(any(TaskEntity.class))).thenReturn(mockTaskDto);

    TaskDto createdTask = taskService.createTask(mockTaskDto);

    // assert and verify
    verify(taskRepository).save(mockTaskEntity);
    assertThat(createdTask).isEqualTo(mockTaskDto);
  }

  @Test
  void testGetTaskById() {
    // create fake dto and entity
    TaskEntity mockTaskEntity = Instancio.create(TaskEntity.class);
    TaskDto mockTaskDto = Instancio.create(TaskDto.class);

    // mock return value of method calls
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTaskEntity));
    when(taskMapper.toDTO(any(TaskEntity.class))).thenReturn(mockTaskDto);

    TaskDto result = taskService.getTaskById(1L);

    // assert and verify
    verify(taskRepository).findById(1L);
    assertThat(result).isEqualTo(mockTaskDto);
  }

  @Test
  void testGetTasksForUser() {
    // create fake dto and entity
    List<TaskEntity> mockTaskEntities = List.of(Instancio.create(TaskEntity.class));
    List<TaskDto> mockTaskDtos = List.of(Instancio.create(TaskDto.class));

    // mock return value of method calls
    when(taskRepository.findAllByAssignee(anyString())).thenReturn(mockTaskEntities);
    when(taskMapper.toDTO(any(TaskEntity.class))).thenReturn(mockTaskDtos.get(0));

    List<TaskDto> tasks = taskService.getTasksForUser();

    // assert
    assertThat(tasks).hasSize(mockTaskDtos.size());
  }

  @Test
  void testUpdateTask() {
    // create fake dto and entity
    TaskDto mockTaskDto = Instancio.create(TaskDto.class);
    TaskEntity mockTaskEntity = Instancio.create(TaskEntity.class);

    mockTaskEntity.setAssignee("testUser");

    // mock return value of method calls
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTaskEntity));
    when(taskMapper.toDTO(any(TaskEntity.class))).thenReturn(mockTaskDto);

    mockTaskEntity.setTitle(mockTaskDto.getTitle());
    mockTaskEntity.setDescription(mockTaskDto.getDescription());
    mockTaskEntity.setDueDate(mockTaskDto.getDueDate());

    when(taskRepository.save(any(TaskEntity.class))).thenReturn(mockTaskEntity);

    TaskDto updatedTask = taskService.updateTask(1L, mockTaskDto);

    // assert and verify
    verify(taskRepository).save(mockTaskEntity);
    assertThat(updatedTask).isEqualTo(mockTaskDto);
  }

  @Test
  void testChangeStatus() {
    // create entities
    TaskEntity mockTaskEntity = Instancio.create(TaskEntity.class);
    TaskStatusEntity mockTaskStatusEntity = Instancio.create(TaskStatusEntity.class);

    // mock retur value of method calles
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTaskEntity));
    when(taskStatusMapper.toEntity(any(TaskStatusDto.class), any(TaskEntity.class)))
        .thenReturn(mockTaskStatusEntity);

    taskService.changeStatus(1L, "IN_PROGRESS");

    // verify
    verify(taskRepository).findById(1L);
    verify(taskStatusRepository).save(mockTaskStatusEntity);
  }

  @Test
  void testAddCommentToTask() {
    // mock dto and entities
    TaskEntity mockTaskEntity = Instancio.create(TaskEntity.class);
    CommentDto mockCommentDto = Instancio.create(CommentDto.class);
    CommentEntity mockCommentEntity = Instancio.create(CommentEntity.class);

    // mock return value of method calls
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTaskEntity));
    when(commentMapper.toEntity(any(CommentDto.class), any(TaskEntity.class)))
        .thenReturn(mockCommentEntity);

    taskService.addCommentToTask(1L, mockCommentDto);

    // verify
    verify(commentRepository).saveAndFlush(mockCommentEntity);
  }

  @Test
  void testDeleteTask() {
    // create entity
    TaskEntity mockTaskEntity = Instancio.create(TaskEntity.class);

    // mock return value of method calls
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTaskEntity));

    taskService.deleteTask(1L);

    // verify
    verify(taskRepository).delete(mockTaskEntity);
  }
}

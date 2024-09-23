package interview.taskorganizer.controllers;

import interview.taskorganizer.dto.CommentDto;
import interview.taskorganizer.dto.TaskDto;
import interview.taskorganizer.dto.TaskStatusDto;
import interview.taskorganizer.services.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  @Autowired private TaskService taskService;

  @PostMapping
  public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
    final var response = taskService.createTask(taskDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
    final var response = taskService.updateTask(id, taskDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}/comment")
  public ResponseEntity<Void> addCommentToTask(
      @PathVariable Long id, @RequestBody CommentDto commentDto) {
    taskService.addCommentToTask(id, commentDto);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}/transition")
  public ResponseEntity<Void> changeStatus(
      @PathVariable Long id, @RequestBody TaskStatusDto taskStatus) {
    taskService.changeStatus(id, taskStatus.getStatus());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
    final var response = taskService.getTaskById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<TaskDto>> getTasksForUser() {
    final var response = taskService.getTasksForUser();
    return ResponseEntity.ok(response);
  }
}

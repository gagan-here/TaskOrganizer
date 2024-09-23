package interview.taskorganizer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TaskDto {
  public TaskDto(String title, String description, LocalDate dueDate) {
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
  }

  private String id;
  private String title;
  private String description;
  private LocalDate dueDate;
  private String assignee;

  private TaskStatusDto taskStatus;
  private List<CommentDto> comments;
  private List<AttachmentDto> attachments;
}

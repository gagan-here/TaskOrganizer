package interview.taskorganizer.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class TaskEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private LocalDate dueDate;
  private String assignee;

  @OneToOne(
      mappedBy = "task",
      orphanRemoval = true,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER)
  @PrimaryKeyJoinColumn
  private TaskStatusEntity taskStatus;

  @OneToMany(
      mappedBy = "task",
      orphanRemoval = true,
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER)
  private List<CommentEntity> comments;

  @OneToMany(
      mappedBy = "task",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<AttachmentEntity> attachments;
}

package interview.taskorganizer.mapper;

import interview.taskorganizer.config.MapperConfiguration;
import interview.taskorganizer.dto.TaskDto;
import interview.taskorganizer.entities.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfiguration.class)
@Component
public interface TaskMapper {
  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "dueDate", target = "dueDate")
  @Mapping(source = "taskStatus", target = "taskStatus")
  @Mapping(source = "comments", target = "comments")
  @Mapping(source = "attachments", target = "attachments")
  TaskDto toDTO(TaskEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "title", target = "title")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "dueDate", target = "dueDate")
  @Mapping(target = "taskStatus", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  TaskEntity toEntity(TaskDto dto);
}

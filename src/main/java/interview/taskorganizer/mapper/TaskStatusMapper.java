package interview.taskorganizer.mapper;

import interview.taskorganizer.config.MapperConfiguration;
import interview.taskorganizer.dto.TaskStatusDto;
import interview.taskorganizer.entities.TaskEntity;
import interview.taskorganizer.entities.TaskStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfiguration.class)
@Component
public interface TaskStatusMapper {
  TaskStatusMapper INSTANCE = Mappers.getMapper(TaskStatusMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "dto.status", target = "status")
  @Mapping(source = "taskEntity", target = "task")
  TaskStatusEntity toEntity(TaskStatusDto dto, TaskEntity taskEntity);
}

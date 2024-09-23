package interview.taskorganizer.mapper;

import interview.taskorganizer.config.MapperConfiguration;
import interview.taskorganizer.dto.CommentDto;
import interview.taskorganizer.entities.CommentEntity;
import interview.taskorganizer.entities.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfiguration.class)
@Component
public interface CommentMapper {
  CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

  @Mapping(source = "review", target = "review")
  @Mapping(source = "reviewer", target = "reviewer")
  CommentDto toDTO(CommentEntity entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "dto.review", target = "review")
  @Mapping(source = "dto.reviewer", target = "reviewer")
  @Mapping(source = "taskEntity", target = "task")
  CommentEntity toEntity(CommentDto dto, TaskEntity taskEntity);
}

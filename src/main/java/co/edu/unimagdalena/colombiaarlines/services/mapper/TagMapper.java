package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TagMapper {

    Tag toEntity(TagCreateRequest req);

    @Mapping(target = "id", ignore = true)
    void updateEntity(TagUpdateRequest req);

    TagResponse toResponse(Tag tag);
}

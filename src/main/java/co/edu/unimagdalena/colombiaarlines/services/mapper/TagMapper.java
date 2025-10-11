package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    Tag toEntity(TagCreateRequest req);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    void updateEntity(TagUpdateRequest req, @MappingTarget Tag tag);

    TagResponse toResponse(Tag tag);
}


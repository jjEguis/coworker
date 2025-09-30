package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;

public class TagMapper {

    public static Tag toEntity(TagDtos.TagCreateRequest req) {
        return Tag.builder()
                .name(req.name())
                .build();
    }

    public static void updateEntity(Tag tag, TagDtos.TagUpdateRequest req) {
        tag.setName(req.name());
    }

    public static TagDtos.TagResponse toResponse(Tag tag) {
        return new TagDtos.TagResponse(
                tag.getId(),
                tag.getName()
        );
    }
}

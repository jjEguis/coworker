package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.TagDtos.TagCreateRequest;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.TagDtos.TagResponse;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.TagDtos.TagUpdateRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagService {

    TagResponse create(TagCreateRequest req);
    Optional<TagResponse> findTagByName(String name);

    List<TagResponse> findTagByNameIn(Collection<String> names);
    
    List<TagResponse> list();

    TagResponse update(Long id, TagUpdateRequest req);

    void delete(Long id);
}
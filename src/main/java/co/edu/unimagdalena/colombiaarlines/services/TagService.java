package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagUpdateRequest;

import java.util.List;

public interface TagService {

    TagResponse create(TagCreateRequest req);

    TagResponse get(Long id);
    
    List<TagResponse> list();

    TagResponse update(Long id, TagUpdateRequest req);

    void delete(Long id);
}
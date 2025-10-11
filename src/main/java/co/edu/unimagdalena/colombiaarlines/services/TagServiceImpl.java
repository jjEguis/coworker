package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.TagRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagResponse create(TagCreateRequest req) {
        Tag tag = tagMapper.toEntity(req);
        return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Override 
    @Transactional(readOnly = true)
    public TagResponse get(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> list() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    @Override
    public TagResponse update(Long id, TagUpdateRequest req) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(id)));

        tagMapper.updateEntity(req, tag);

        return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public void delete(Long id) {
        // Lógica de negocio: Borrar solo si existe.
        if (!tagRepository.existsById(id)) {
            throw new NotFoundException("Tag %d not found".formatted(id));
        }
        tagRepository.deleteById(id);
    }
}
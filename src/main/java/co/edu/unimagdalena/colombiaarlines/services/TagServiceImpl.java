package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.TagRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagResponse create(TagCreateRequest req) {
        // Validar si ya existe un tag con ese nombre
        tagRepository.findTagByName(req.name()).ifPresent(t -> {
            throw new RuntimeException("Ya existe un tag con el nombre: " + req.name());
        });

        Tag tag = Tag.builder()
                .name(req.name())
                .build();

        return TagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public Optional<TagResponse> findTagByName(String name) {
        return tagRepository.findTagByName(name)
                .map(TagMapper::toResponse);
    }

    @Override
    public List<TagResponse> findTagByNameIn(Collection<String> names) {
        return tagRepository.findTagByNameIn(names)
                .stream()
                .map(TagMapper::toResponse)
                .toList();
    }

    @Override
    public List<TagResponse> list() {
        return tagRepository.findAll()
                .stream()
                .map(TagMapper::toResponse)
                .toList();
    }

    @Override
    public TagResponse update(Long id, TagUpdateRequest req) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrado con id: " + id));

        tag.setName(req.name());
        return TagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("No se encontró el tag con id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
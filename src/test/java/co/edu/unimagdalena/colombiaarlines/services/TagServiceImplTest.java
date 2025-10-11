package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.TagDtos.TagUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.TagRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.TagServiceImpl;
import co.edu.unimagdalena.colombiaarlines.services.mapper.TagMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    TagRepository repo;
    @Mock
    TagMapper mapper;
    @InjectMocks
    TagServiceImpl service; 

    private final Long TAG_ID = 1L;
    private final String TAG_NAME = "Vuelo Nacional";

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new TagCreateRequest(TAG_NAME);
        var tagToSave = Tag.builder().name(TAG_NAME).build();
        var savedTag = Tag.builder().id(TAG_ID).name(TAG_NAME).build();
        var expectedResponse = new TagResponse(TAG_ID, TAG_NAME);

        when(mapper.toEntity(req)).thenReturn(tagToSave);
        // ThenAnswer para verificar que el repositorio asigna un ID al guardar
        when(repo.save(tagToSave)).thenAnswer(inv -> {
            Tag t = inv.getArgument(0);
            t.setId(TAG_ID); 
            return t;
        });
        when(mapper.toResponse(savedTag)).thenReturn(expectedResponse);

        var result = service.create(req);

        assertThat(result.id()).isEqualTo(TAG_ID);
        assertThat(result.name()).isEqualTo(TAG_NAME);
        verify(repo).save(tagToSave); // Verifica que se llamó al repo con la entidad correcta
    }

    @Test
    void shouldFindByIdAndReturnResponseDto() {
        var foundTag = Tag.builder().id(TAG_ID).name(TAG_NAME).build();
        var expectedResponse = new TagResponse(TAG_ID, TAG_NAME);

        when(repo.findById(TAG_ID)).thenReturn(Optional.of(foundTag));
        when(mapper.toResponse(foundTag)).thenReturn(expectedResponse);

        var result = service.get(TAG_ID);
        assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingNonExistentId() {
        when(repo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(TAG_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(TAG_ID.toString());
    }

    @Test
    void shouldReturnListOfResponseDtos() {
        var tag1 = Tag.builder().id(1L).name("A").build();
        var tag2 = Tag.builder().id(2L).name("B").build();
        var tags = List.of(tag1, tag2);
        
        var res1 = new TagResponse(1L, "A");
        var res2 = new TagResponse(2L, "B");
        var expectedList = List.of(res1, res2);

        // Mockearr!!!!
        when(repo.findAll()).thenReturn(tags);
        when(mapper.toResponse(tag1)).thenReturn(res1);
        when(mapper.toResponse(tag2)).thenReturn(res2);

        // ¡El resultado que todos esperamos!
        var result = service.list();
        assertThat(result).hasSize(2).containsExactlyElementsOf(expectedList);
    }

    @Test
    void shouldUpdateTagAndReturnResponseDto() {
        var req = new TagUpdateRequest("Etiqueta Actualizada");
        var existingTag = Tag.builder().id(TAG_ID).name(TAG_NAME).build();
        var updatedTag = Tag.builder().id(TAG_ID).name("Etiqueta Actualizada").build();
        var expectedResponse = new TagResponse(TAG_ID, "Etiqueta Actualizada");

        when(repo.findById(TAG_ID)).thenReturn(Optional.of(existingTag));
        when(repo.save(existingTag)).thenReturn(updatedTag);
        when(mapper.toResponse(updatedTag)).thenReturn(expectedResponse);

        var result = service.update(TAG_ID, req);

        verify(mapper).updateEntity(req, existingTag); // Verificar que MapStruct fue llamado para actualizar la entidad
        assertThat(result.id()).isEqualTo(TAG_ID);
        assertThat(result.name()).isEqualTo("Etiqueta Actualizada");
    }

    @Test
    void shouldDeleteTagSuccessfully() {
        // Simula que la entidad existe
        when(repo.existsById(TAG_ID)).thenReturn(true);

        service.delete(TAG_ID);

        verify(repo).deleteById(TAG_ID);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentId() {
        when(repo.existsById(TAG_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(TAG_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(TAG_ID.toString());
        
        verify(repo, never()).deleteById(anyLong());
    }
}
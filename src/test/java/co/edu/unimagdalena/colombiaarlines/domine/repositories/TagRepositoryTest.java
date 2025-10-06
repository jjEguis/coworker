package co.edu.unimagdalena.colombiaarlines.domine.repositories;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Collection;

@DataJpaTest
class TagRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("Tag: encuentra un tag por su nombre")
    void shouldFindTagByName() {
        // Given
        Tag tag = Tag.builder().name("Vacaciones").build();

        // When
        tagRepository.save(tag);
        Optional<Tag> foundTag = tagRepository.findTagByName("Vacaciones");

        // Then
        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getName()).isEqualTo("Vacaciones");
    }

    @Test
    @DisplayName("Tag: encuentra tags por una lista de nombres")
    void shouldFindTagsByNameIn() {
        // Given
        tagRepository.save(Tag.builder().name("Romántico").build());
        tagRepository.save(Tag.builder().name("Aventura").build());
        tagRepository.save(Tag.builder().name("Negocios").build());

        Collection<String> namesToFind = Arrays.asList("Aventura", "Romántico");

        // When
        List<Tag> foundTags = tagRepository.findTagByNameIn(namesToFind);

        // Then
        assertThat(foundTags).hasSize(2);
        assertThat(foundTags)
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Aventura", "Romántico");
    }
}
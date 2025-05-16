package ru.bichevoy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bichevoy.entity.Tag;
import ru.bichevoy.repository.TagRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private TagService tagService;

    @Test
    void saveTag() {
        Tag tag = new Tag();
        tag.setName("tag");
        doReturn(tag).when(tagRepository).save(tag);

        Tag saveTag = tagService.saveTag(tag);
        assertEquals(tag, saveTag);
        verify(tagRepository).save(tag);
    }

    @Test
    void findTagByName() {
        Tag tag = new Tag();
        tag.setName("tag");
        doReturn(Optional.of(tag)).when(tagRepository).findByName("tag");

        Optional<Tag> tagByName = tagService.findTagByName("tag");
        assertTrue(tagByName.isPresent());
        assertEquals(tag, tagByName.get());

        verify(tagRepository).findByName("tag");
    }
}
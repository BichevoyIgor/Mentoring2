package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.Tag;
import ru.bichevoy.repository.TagRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Optional<Tag> findTagByName(String tagName) {
        return tagRepository.findByName(tagName);
    }
}

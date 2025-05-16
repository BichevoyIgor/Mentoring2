package ru.bichevoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bichevoy.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String tagName);

}

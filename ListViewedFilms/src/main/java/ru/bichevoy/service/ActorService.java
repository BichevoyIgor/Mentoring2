package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.ActorDTOResponse;
import ru.bichevoy.entity.Actor;
import ru.bichevoy.repository.ActorRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    public Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public Optional<Actor> findActorByName(String actor) {
        return actorRepository.findActorByName(actor);
    }

    public Page<ActorDTOResponse> findAllActorsDTOResponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return actorRepository.findAll(pageable)
                .map(ActorDTOResponse::new);
    }
}

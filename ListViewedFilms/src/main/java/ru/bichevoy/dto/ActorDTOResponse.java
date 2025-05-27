package ru.bichevoy.dto;

import ru.bichevoy.entity.Actor;

import java.io.Serializable;

public record ActorDTOResponse(long id, String name) implements Serializable {

    public ActorDTOResponse(Actor actor) {
        this(actor.getId(), actor.getName());
    }

}

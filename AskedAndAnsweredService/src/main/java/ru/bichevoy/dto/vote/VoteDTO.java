package ru.bichevoy.dto.vote;

import lombok.Getter;
import lombok.Setter;
import ru.bichevoy.entity.VoteType;

@Setter
@Getter
public class VoteDTO {
    private Long id;
    private VoteType voteType;
    private String userName;
    private Long questionId;
    private Long answerId;
}

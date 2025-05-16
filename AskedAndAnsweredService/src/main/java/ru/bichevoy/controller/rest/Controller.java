package ru.bichevoy.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bichevoy.dto.question.QuestionDTOMapper;
import ru.bichevoy.dto.question.QuestionRegistrationDTO;
import ru.bichevoy.dto.question.QuestionResponseDTO;
import ru.bichevoy.entity.Question;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.service.QuestionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Controller {

    private final QuestionService questionService;
    private final QuestionDTOMapper questionDTOMapper;

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestion() {
        List<QuestionResponseDTO> allQuestionsResponseDTO = questionService.findAllQuestionsResponseDTO();
        return new ResponseEntity<>(allQuestionsResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable Long id) {
        return new ResponseEntity<>(questionService.findQuestionDTOById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Error: вопрос с id=%d не найден", id))),
                HttpStatus.OK);
    }

    @PostMapping("/questions")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<QuestionResponseDTO> createNewQuestion(@Valid @RequestBody QuestionRegistrationDTO questionDTO) {
        Optional<Question> question = questionService.saveNewQuestionDTO(questionDTO);
        QuestionResponseDTO questionRequestDTO = questionDTOMapper.getQuestionRequestDTO(question.get());
        return new ResponseEntity<>(questionRequestDTO, HttpStatus.CREATED);
    }

    @PutMapping("/questions")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<QuestionResponseDTO> changeQuestion(@Valid @RequestBody QuestionRegistrationDTO questionDTO) {
        Question question = questionService.updateQuestionFromDTO(questionDTO);
        return new ResponseEntity<>(questionDTOMapper.getQuestionRequestDTO(question), HttpStatus.OK);
    }

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

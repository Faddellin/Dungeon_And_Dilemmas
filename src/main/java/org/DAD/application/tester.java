package org.DAD.application;

import lombok.AllArgsConstructor;
import org.DAD.application.model.Answer.TextAnswerModel;
import org.DAD.application.model.Question.QuestionModel;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.domain.entity.Answer.TextAnswer;
import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.mapper.AnswerMapper;
import org.DAD.domain.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class tester implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<Question> questionO = questionRepository.findById(UUID.fromString("7c8ee70f-d178-4b5b-9bfd-3666485a4d0c"));



       // QuestionModel q = QuestionMapper.INSTANCE.questionToQuestionModel(first);

    }
}

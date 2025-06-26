package org.DAD.domain.mapper;

import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizModel;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.Quiz.QuizDifficulty;
import org.DAD.domain.entity.Quiz.QuizStatus;
import org.DAD.domain.entity.User.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface QuizMapper {
    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "difficulty", source = "quizDifficulty")
    @Mapping(target = "status", source = "quizStatus")
    @Mapping(target = "creator", source = "user")
    Quiz quizCreateModelToQuiz(QuizCreateModel quizCreateModel,
                               QuizDifficulty quizDifficulty,
                               QuizStatus quizStatus,
                               User user);

    @Mapping(target = "userShortModel", source = "quiz.creator")
    QuizModel quizToQuizModel(Quiz quiz);
}

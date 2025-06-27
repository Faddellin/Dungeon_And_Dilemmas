package org.DAD.domain.mapper;

import org.DAD.application.model.Question.QuestionCreateModel;
import org.DAD.application.model.Question.QuestionModel;
import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.entity.Quiz.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quiz", source = "quiz")
    ChoiceQuestion questionCreateModelToChoiceQuestion(QuestionCreateModel questionCreateModel,
                                                       Quiz quiz);

    @Mapping(target = "answers", ignore = true)
    QuestionModel questionToQuestionModel(Question question);
}

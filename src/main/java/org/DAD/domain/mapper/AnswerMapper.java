package org.DAD.domain.mapper;

import org.DAD.application.model.Answer.TextAnswerCreateModel;
import org.DAD.application.model.Answer.TextAnswerModel;
import org.DAD.domain.entity.Answer.TextAnswer;
import org.DAD.domain.entity.Question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isCorrect", source = "isCorrect")
    @Mapping(target = "question", source = "question")
    TextAnswer textAnswerCreateModelToTextAnswer(TextAnswerCreateModel textAnswerCreateModel,
                                                 Question question,
                                                 Boolean isCorrect);

    TextAnswerModel textAnswerToTextAnswerModel(TextAnswer textAnswer);
}

package org.DAD.domain.mapper;

import org.DAD.application.model.Answer.TextAnswerCreateModel;
import org.DAD.application.model.User.UserModel;
import org.DAD.domain.entity.Answer.TextAnswer;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.entity.User.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel userToUserModel(User user);
}

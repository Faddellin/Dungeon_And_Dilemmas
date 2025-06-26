package org.DAD.domain.mapper;

import org.DAD.application.model.Group.GroupModel;
import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizModel;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.Quiz.QuizDifficulty;
import org.DAD.domain.entity.Quiz.QuizStatus;
import org.DAD.domain.entity.User.User;
import org.apache.catalina.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(target = "members", source = "members")
    GroupModel groupToGroupModel(ChatGroup group);

}

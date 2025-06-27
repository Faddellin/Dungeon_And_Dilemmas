package org.DAD.domain.mapper;

import org.DAD.application.model.Group.GroupModel;
import org.DAD.domain.entity.Group.ChatGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {QuizMapper.class})
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(target = "members", source = "members")
    @Mapping(target = "selectedQuizId", source = "quiz.id")
    @Mapping(target = "ownerId", source = "ownerId")
    GroupModel groupToGroupModel(ChatGroup group);

}

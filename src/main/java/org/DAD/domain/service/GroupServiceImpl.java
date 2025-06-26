package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromBack.PlayerJoinedMessage;
import org.DAD.application.model.Group.GroupModel;
import org.DAD.application.repository.*;
import org.DAD.application.service.ConnectionService;
import org.DAD.application.service.GroupService;
import org.DAD.application.util.CodeGenerator;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.mapper.GroupMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository _groupRepository;
    private final UserRepository _userRepository;
    private final CodeGenerator _codeGenerator;
    private final ConnectionService _connectionService;

    public GroupServiceImpl(
            GroupRepository groupRepository,
            UserRepository userRepository,
            CodeGenerator codeGenerator,
            ConnectionService connectionService) {
        _groupRepository = groupRepository;
        _userRepository = userRepository;
        _codeGenerator = codeGenerator;
        _connectionService = connectionService;
    }

    public GroupModel createGroup(UUID userId)throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not exists");
            throw entityNotFoundException;
        }

        User user = userO.get();

        if(user.getCurrentGroup() != null){
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("User", "The user is already in the group");
            throw badRequestException;
        }

        String groupCode = _codeGenerator.generateUniqueCode();

        ChatGroup group = new ChatGroup();
        group.setCode(groupCode);
        _groupRepository.save(group);
        _groupRepository.flush();

        user.setCurrentGroup(group);
        _userRepository.flush();

        group.setMembers(List.of(user));

        return GroupMapper.INSTANCE.groupToGroupModel(group);
    }

    public GroupModel joinGroup(UUID userId, String code)throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);
        Optional<ChatGroup> groupO = _groupRepository.findByCode(code);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(groupO.isEmpty()){
                entityNotFoundException.addError("Group", "Group is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        ChatGroup group = groupO.get();

        if(user.getCurrentGroup() != null){
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("User", "The user is already in the group");
            throw badRequestException;
        }

        user.setCurrentGroup(group);
        _userRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerJoinedMessage
                        .builder()
                        .joinedPlayerName(user.getUserName())
                        .joinedPlayerId(user.getId())
                        .build());

        return GroupMapper.INSTANCE.groupToGroupModel(group);
    }

    public GroupModel setPlayerIsReady(UUID userId, String code)throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);
        Optional<ChatGroup> groupO = _groupRepository.findByCode(code);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(groupO.isEmpty()){
                entityNotFoundException.addError("Group", "Group is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        ChatGroup group = groupO.get();

        if(user.getCurrentGroup() != null){
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("User", "The user is already in the group");
            throw badRequestException;
        }

        user.setCurrentGroup(group);
        _userRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerJoinedMessage
                        .builder()
                        .joinedPlayerName(user.getUserName())
                        .joinedPlayerId(user.getId())
                        .build());

        return GroupMapper.INSTANCE.groupToGroupModel(group);
    }

    public void leftGroup(UUID userId) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(userId);

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not exists");
            throw entityNotFoundException;
        }

        User user = userO.get();
        ChatGroup group = user.getCurrentGroup();

        user.setCurrentGroup(null);

        if (group.getMembers().size() == 1) {
            _groupRepository.delete(group);
        }

        _groupRepository.flush();
        _userRepository.flush();
    }

}

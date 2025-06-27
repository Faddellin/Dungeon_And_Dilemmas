package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromBack.PlayerJoinedMessage;
import org.DAD.application.model.Connection.PlayerIsReadyMessage;
import org.DAD.application.model.Connection.PlayerLeftMessage;
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

import java.util.*;

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
        group.setUsersReady(Map.of(user.getId(), false));
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
        group.getUsersReady().put(user.getId(), false);
        user.setCurrentGroup(group);
        _userRepository.flush();
        _groupRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerJoinedMessage
                        .builder()
                        .joinedPlayerName(user.getUserName())
                        .joinedPlayerId(user.getId())
                        .playerIsReady(false)
                        .build());

        return GroupMapper.INSTANCE.groupToGroupModel(group);
    }

    public void setPlayerIsReady(PlayerIsReadyMessage playerIsReadyMessage)throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(UUID.fromString(playerIsReadyMessage.getPlayerId()));

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not exists");
            throw entityNotFoundException;
        }

        User user = userO.get();
        ChatGroup group = user.getCurrentGroup();

        if(group == null){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not in group");
            throw entityNotFoundException;
        }
        Map<UUID, Boolean> readyMap = new HashMap<>(group.getUsersReady());
        readyMap.put(user.getId(), playerIsReadyMessage.getIsReady());
        group.setUsersReady(readyMap);
        _userRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerIsReadyMessage
                        .builder()
                        .readyPlayerId(user.getId())
                        .isReady(playerIsReadyMessage.getIsReady())
                        .build());

        Boolean needToStartGame = true;
        for(var a : readyMap.values()){
            if(!a){
                needToStartGame = false;
                return;
            }
        }
        if(needToStartGame){
            //TO-DO
            //Добавить запуск таймера, который также отправит на фронт модель о том, что игра началась
        }

    }

    public void leftGroup(PlayerLeftMessage playerLeftMessage) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(UUID.fromString(playerLeftMessage.getPlayerId()));

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not exists");
            throw entityNotFoundException;
        }

        User user = userO.get();
        ChatGroup group = user.getCurrentGroup();

        user.setCurrentGroup(null);

        Map<UUID, Boolean> readyMap = new HashMap<>(group.getUsersReady());
        readyMap.remove(user.getId());
        group.setUsersReady(readyMap);
        Map<UUID, UUID> answersMap = new HashMap<>(group.getUsersAnswers());
        readyMap.remove(user.getId());
        group.setUsersAnswers(answersMap);

        if (group.getMembers().size() == 1) {
            _groupRepository.delete(group);
        }

        _groupRepository.flush();
        _userRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerLeftMessage
                        .builder()
                        .leftPlayerId(user.getId())
                        .build());
    }

}

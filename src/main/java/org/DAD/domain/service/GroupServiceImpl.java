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
import org.DAD.application.session.GameSession;
import org.DAD.application.session.GameSessionFactory;
import org.DAD.application.util.CodeGenerator;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Group.GroupStatus;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.mapper.GroupMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository _groupRepository;
    private final UserRepository _userRepository;
    private final QuizRepository _quizRepository;
    private final CodeGenerator _codeGenerator;
    private final ConnectionService _connectionService;
    private final GameSessionFactory _gameSessionFactory;

    public GroupServiceImpl(
            GroupRepository groupRepository,
            UserRepository userRepository,
            QuizRepository quizRepository,
            CodeGenerator codeGenerator,
            ConnectionService connectionService,
            GameSessionFactory gameSessionFactory) {
        _groupRepository = groupRepository;
        _userRepository = userRepository;
        _quizRepository = quizRepository;
        _codeGenerator = codeGenerator;
        _connectionService = connectionService;
        _gameSessionFactory = gameSessionFactory;
    }

    @Transactional
    public GroupModel createGroup(UUID userId) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User doesn't exist");
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
        group.setOwnerId(user.getId());
        group.setMembers(List.of(user));
        group.setGroupStatus(GroupStatus.menu);
        _groupRepository.save(group);
        _groupRepository.flush();

        user.setCurrentGroup(group);
        _userRepository.save(user);
        _userRepository.flush();

        return GroupMapper.INSTANCE.groupToGroupModel(group);
    }

    @Transactional
    public GroupModel joinGroup(UUID userId, String code)throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);
        Optional<ChatGroup> groupO = _groupRepository.findByCode(code.toLowerCase());

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User doesn't exist");
            }
            if(groupO.isEmpty()){
                entityNotFoundException.addError("Group", "Group doesn't exist");
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
        if(group.getGroupStatus() == GroupStatus.playing){
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("Group", "The group is playing, you can't join it");
            throw badRequestException;
        }

        Map<UUID, Boolean> readyMap = new HashMap<>(group.getUsersReady());
        readyMap.put(user.getId(), false);
        group.setUsersReady(readyMap);
        List<User> currentMembers = group.getMembers();
        currentMembers.add(user);
        group.setMembers(currentMembers);
        user.setCurrentGroup(group);
        _groupRepository.flush();
        _userRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerJoinedMessage
                        .builder()
                        .joinedPlayerName(user.getUserName())
                        .joinedPlayerId(user.getId())
                        .playerIsReady(false)
                        .build());

        return GroupMapper.INSTANCE.groupToGroupModel(group);
    }

    public GroupModel getGroup(UUID userId) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(userId);
        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User doesn't exist");
            throw entityNotFoundException;
        }

        User user = userO.get();

        return GroupMapper.INSTANCE.groupToGroupModel(user.getCurrentGroup());
    }

    public void setPlayerIsReady(PlayerIsReadyMessage playerIsReadyMessage)throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(UUID.fromString(playerIsReadyMessage.getPlayerId()));
        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User doesn't exist");
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
        _groupRepository.save(group);
        _groupRepository.flush();

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
        if(group.getQuiz() == null){
            needToStartGame = false;
        }
        if(needToStartGame){
            GameSession gs = _gameSessionFactory.createSession(group.getId());
            gs.startGame();
        }

    }

    @Transactional
    public void leftGroup(PlayerLeftMessage playerLeftMessage) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(UUID.fromString(playerLeftMessage.getPlayerId()));

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User doesn't exist");
            throw entityNotFoundException;
        }

        User user = userO.get();
        ChatGroup group = user.getCurrentGroup();

        user.setCurrentGroup(null);

        if(group.getUsersReady() != null){
            Map<UUID, Boolean> readyMap = new HashMap<>(group.getUsersReady());
            readyMap.remove(user.getId());
            group.setUsersReady(readyMap);
        }
        if(group.getUsersAnswers() != null){
            Map<UUID, UUID> answersMap = new HashMap<>(group.getUsersAnswers());
            answersMap.remove(user.getId());
            group.setUsersAnswers(answersMap);
        }


        if (group.getMembers().size() == 1 || group.getOwnerId().equals(user.getId())) {
            group.getMembers().forEach(m -> m.setCurrentGroup(null));
            _userRepository.flush();
            _groupRepository.delete(group);
        }

        _userRepository.flush();
        _groupRepository.flush();

        _connectionService.sendMessageToGroup(group.getId(),
                PlayerLeftMessage
                        .builder()
                        .leftPlayerId(user.getId())
                        .build());
    }

    @Override
    @Transactional
    public void selectQuiz(UUID userId, UUID quizId) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);

        if(userO.isEmpty()) {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not exists");
            throw entityNotFoundException;
        }

        User user = userO.get();
        if(user.getCurrentGroup() == null) {
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("User", "User is not in a group");
        }

        ChatGroup group = user.getCurrentGroup();
        if (!group.getOwnerId().equals(user.getId())) {
            ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
            accessDeniedException.addError("User", "User is not the owner of the group");
            throw accessDeniedException;
        }

        Optional<Quiz> quizO = _quizRepository.findById(quizId);
        if(quizO.isEmpty()) {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("Quiz", "Quiz doesn't exist");
            throw entityNotFoundException;
        }

        Quiz quiz = quizO.get();

        group.setQuiz(quiz);
        _groupRepository.flush();

        Map<UUID, Boolean> readyMap = new HashMap<>(group.getUsersReady());

        Boolean needToStartGame = true;
        for(var a : readyMap.values()){
            if(!a){
                needToStartGame = false;
                return;
            }
        }
        if(group.getQuiz() == null){
            needToStartGame = false;
        }
        if(needToStartGame){
            GameSession gs = _gameSessionFactory.createSession(group.getId());
            gs.startGame();
        }
    }

}

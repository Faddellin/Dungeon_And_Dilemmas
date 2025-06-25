package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.repository.*;
import org.DAD.application.service.GroupService;
import org.DAD.application.util.CodeGenerator;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.User.User;
import org.apache.catalina.Group;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository _groupRepository;
    private final UserRepository _userRepository;
    private final CodeGenerator _codeGenerator;

    public GroupServiceImpl(
            GroupRepository groupRepository,
            UserRepository userRepository,
            CodeGenerator codeGenerator) {
        _groupRepository = groupRepository;
        _userRepository = userRepository;
        _codeGenerator = codeGenerator;
    }

    public String createGroup(UUID userId)throws ExceptionWrapper {
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

        user.setCurrentGroup(group);
        _groupRepository.save(group);

        _userRepository.flush();
        _groupRepository.flush();


        return groupCode;
    }

    public void joinGroup(UUID userId, String code)throws ExceptionWrapper {
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

        if(user.getCurrentGroup() != null){
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            badRequestException.addError("User", "The user is already in the group");
            throw badRequestException;
        }

        user.setCurrentGroup(groupO.get());
        _userRepository.flush();
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

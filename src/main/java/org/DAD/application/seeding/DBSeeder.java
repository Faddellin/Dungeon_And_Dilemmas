package org.DAD.application.seeding;

import jakarta.transaction.Transactional;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.UserRepository;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.User.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {

    private final GroupRepository _groupRepository;
    private final UserRepository _userRepository;

    public DBSeeder(final GroupRepository groupRepository,
                   final UserRepository userRepository) {
        _groupRepository = groupRepository;
        _userRepository = userRepository;
    }

    @Transactional
    public void run(final String[] args) {
        List<ChatGroup> groups = _groupRepository.findAll();
        
        for (ChatGroup group : groups) {
            List<User> users = _userRepository.findByCurrentGroup(group);
            for (User user : users) {
                user.setCurrentGroup(null);
                _userRepository.save(user);
            }
        }

        _groupRepository.deleteAll();
    }
}

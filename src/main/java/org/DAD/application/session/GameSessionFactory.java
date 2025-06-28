package org.DAD.application.session;

import lombok.AllArgsConstructor;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.service.ConnectionService;
import org.DAD.application.service.GameAnsweringService;
import org.DAD.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Executors;

@Component
@AllArgsConstructor
public class GameSessionFactory {
    private final GroupRepository groupRepository;
    private final ConnectionService connectionService;
    private final GameAnsweringService gameAnsweringService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public GameSession createSession(UUID groupId) {
        return new GameSession(
                groupId,
                groupRepository,
                connectionService,
                gameAnsweringService,
                questionRepository,
                userRepository
        );
    }
}

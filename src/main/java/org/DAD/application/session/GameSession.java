package org.DAD.application.session;

import lombok.AllArgsConstructor;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.QuizRepository;
import org.DAD.application.service.ConnectionService;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Question.Question;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class GameSession {
    private final UUID groupId;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final GroupRepository _groupRepository;
    private final QuizRepository _quizRepository;
    private final ConnectionService _connectionService;
    private Integer currentQuestionNumber = 1;

    public GameSession(UUID groupId, GroupRepository groupRepository, ConnectionService connectionService, QuizRepository quizRepository) {
        this.groupId = groupId;
        this._groupRepository = groupRepository;
        this._connectionService = connectionService;
        this._quizRepository = quizRepository;
    }

    public void startQuestions() {
        Question currentQuestion = _quizRepository.;
    }

    private Boolean checkAllPlayersReady() {
        ChatGroup group = _groupRepository.findById(groupId).get();
        Map<UUID, Boolean> playersReady = group.getUsersReady();

        for(var playerReady : playersReady.values()) {
            if (!playerReady) {
                return false;
            }
        }

        return true;
    }

    public void startGame() {
        scheduler.schedule(() -> {
            if (!checkAllPlayersReady()) {
                scheduler.shutdown();
            } else {
                startQuestions();
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}

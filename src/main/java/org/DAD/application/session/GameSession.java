package org.DAD.application.session;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromBack.AnswerResultMessage;
import org.DAD.application.model.Connection.FromBack.QuestionMessage;
import org.DAD.application.model.Question.QuestionModel;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.application.repository.QuizRepository;
import org.DAD.application.service.ConnectionService;
import org.DAD.application.service.GameAnsweringService;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.mapper.QuestionMapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class GameSession {
    private UUID groupId;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private GroupRepository _groupRepository;
    private QuestionRepository _questionRepository;
    private ConnectionService _connectionService;
    private GameAnsweringService _gameAnsweringService;
    private Integer currentQuestionNumber = 1;

    public GameSession(UUID groupId, GroupRepository groupRepository, ConnectionService connectionService, GameAnsweringService gameAnsweringService, QuestionRepository questionRepository) {
        this.groupId = groupId;
        this._connectionService = connectionService;
        this._gameAnsweringService = gameAnsweringService;
        this._groupRepository = groupRepository;
        this._questionRepository = questionRepository;
    }

    private void goToNextQuestion() {
        QuestionModel nextQuestion = getNextQuestion();
        if(nextQuestion == null) {
            endGame();
            return;
        }else{

            _connectionService.sendMessageToGroup(groupId, QuestionMessage.builder().questionModel(nextQuestion).build());
            scheduler.schedule(() -> {
                try {
                    sendAnswersAndGoToNextQuestion();
                }catch (Exception e) {
                    return;
                }

            }, nextQuestion.getDuration(), TimeUnit.SECONDS);
        }
    }

    private void sendAnswersAndGoToNextQuestion() throws ExceptionWrapper {
        _gameAnsweringService.sendAnswerResults(groupId.toString());
        scheduler.schedule(this::goToNextQuestion, 5, TimeUnit.SECONDS);
    }

    @Transactional
    protected QuestionModel getNextQuestion(){
        ChatGroup group = _groupRepository.findById(groupId).get();
        List<Question> questions = _questionRepository.findAllByQuiz(group.getQuiz());
        Optional<Question> questionO = questions
                .stream()
                .filter(f -> f.getQuestionNumber().equals(currentQuestionNumber))
                .findFirst();
        if(questionO.isEmpty()){
            return null;
        }else{
            group.setCurrentQuestion(questionO.get());
            _groupRepository.save(group);
            _groupRepository.flush();
            currentQuestionNumber++;
            return QuestionMapper.INSTANCE.questionToQuestionModel(questionO.get());
        }
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
            if (checkAllPlayersReady()) {
                goToNextQuestion();
            } else {
                return;
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void endGame() {

    }
}

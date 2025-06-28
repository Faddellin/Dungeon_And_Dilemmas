package org.DAD.application.session;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromBack.AnswerResultMessage;
import org.DAD.application.model.Connection.FromBack.GameEndedMessage;
import org.DAD.application.model.Connection.FromBack.QuestionMessage;
import org.DAD.application.model.Question.QuestionModel;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.application.repository.QuizRepository;
import org.DAD.application.repository.ResultRepository;
import org.DAD.application.service.ConnectionService;
import org.DAD.application.service.GameAnsweringService;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.entity.Result.Result;
import org.DAD.domain.entity.User.User;
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
    private ResultRepository _resultRepository;
    private ConnectionService _connectionService;
    private GameAnsweringService _gameAnsweringService;
    private Integer currentQuestionNumber = 1;
    private Map<UUID, Integer> _playersScores = new HashMap<>();

    public GameSession(UUID groupId, GroupRepository groupRepository, ConnectionService connectionService, 
                      GameAnsweringService gameAnsweringService, QuestionRepository questionRepository,
                      ResultRepository resultRepository) {
        this.groupId = groupId;
        this._connectionService = connectionService;
        this._gameAnsweringService = gameAnsweringService;
        this._groupRepository = groupRepository;
        this._questionRepository = questionRepository;
        this._resultRepository = resultRepository;
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
        calculateScoresForCurrentQuestion();
        
        _gameAnsweringService.sendAnswerResults(groupId.toString());
        scheduler.schedule(this::goToNextQuestion, 5, TimeUnit.SECONDS);
    }

    private void calculateScoresForCurrentQuestion() {
        try {
            ChatGroup group = _groupRepository.findById(groupId).get();
            Question currentQuestion = group.getCurrentQuestion();
            
            if (currentQuestion instanceof ChoiceQuestion choiceQuestion) {
                Map<UUID, UUID> usersAnswers = group.getUsersAnswers();
                UUID correctAnswerId = choiceQuestion.getRightAnswerId();
                
                if (usersAnswers != null && correctAnswerId != null) {
                    for (Map.Entry<UUID, UUID> entry : usersAnswers.entrySet()) {
                        UUID userId = entry.getKey();
                        UUID userAnswerId = entry.getValue();
                        
                        if (correctAnswerId.equals(userAnswerId)) {
                            Integer currentScore = _playersScores.getOrDefault(userId, 0);
                            _playersScores.put(userId, currentScore + currentQuestion.getReward());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculating scores: " + e.getMessage());
        }
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
            group.setUsersAnswers(new HashMap<>());
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
        saveResultsToDatabase();
        
        _connectionService.sendMessageToGroup(groupId, GameEndedMessage.builder().scores(_playersScores).build());
    }

    @Transactional
    protected void saveResultsToDatabase() {
        try {
            ChatGroup group = _groupRepository.findById(groupId).get();
            
            for (User user : group.getMembers()) {
                Integer score = _playersScores.getOrDefault(user.getId(), 0);
                
                Result.ResultId resultId = new Result.ResultId(user.getId(), group.getQuiz().getId());
                
                Optional<Result> existingResult = _resultRepository.findById(resultId);
                
                Result result;
                if (existingResult.isPresent()) {
                    result = existingResult.get();
                    result.setScore(score);
                } 
                else {
                    result = new Result();
                    result.setUser(user);
                    result.setQuiz(group.getQuiz());
                    result.setScore(score);
                }
                
                _resultRepository.save(result);
            }
            
            _resultRepository.flush();
        } catch (Exception e) {
            System.err.println("Error saving results to database: " + e.getMessage());
        }
    }
}

package org.DAD.domain.service;

import lombok.AllArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.repository.AnswerRepository;
import org.DAD.application.repository.ChoiceQuestionRepository;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.application.repository.TextAnswerRepository;
import org.DAD.application.service.ConnectionService;
import org.DAD.application.service.GameAnsweringService;
import org.DAD.domain.entity.Answer.TextAnswer;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.DAD.application.model.Connection.FromBack.AnswerResultMessage;
import org.DAD.domain.entity.User.User;
import org.apache.coyote.BadRequestException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GameAnsweringServiceImpl implements GameAnsweringService {
    private final ChoiceQuestionRepository choiceQuestionRepository;
    private final TextAnswerRepository textAnswerRepository;
    private final GroupRepository groupRepository;
    private final ConnectionService _connectionService;

    @Override
    public Boolean isAnswerCorrect(UUID questionId, UUID answerId) throws ExceptionWrapper {
        Optional<ChoiceQuestion> choiceQuestionOpt = choiceQuestionRepository.findById(questionId);
        if (choiceQuestionOpt.isEmpty()) {
            ExceptionWrapper badRequestEx = new ExceptionWrapper(new BadRequestException());
            badRequestEx.addError("Question ID", "Question not found or is not a choice question.");
            throw badRequestEx;
        }
        ChoiceQuestion choiceQuestion = choiceQuestionOpt.get();
        return answerId.equals(choiceQuestion.getRightAnswerId());
    }

    @Override
    public TextAnswer getCorrectAnswer(UUID questionId) throws ExceptionWrapper {
        Optional<ChoiceQuestion> choiceQuestionOpt = choiceQuestionRepository.findById(questionId);
        if (choiceQuestionOpt.isEmpty()) {
            ExceptionWrapper badRequestEx = new ExceptionWrapper(new BadRequestException());
            badRequestEx.addError("Question ID", "Question not found or is not a choice question.");
            throw badRequestEx;
        }
        ChoiceQuestion choiceQuestion = choiceQuestionOpt.get();

        UUID rightAnswerId = choiceQuestion.getRightAnswerId();
        if (rightAnswerId == null) {
            ExceptionWrapper badRequestEx = new ExceptionWrapper(new BadRequestException());
            badRequestEx.addError("Question", "Correct answer is not set for this question.");
            throw badRequestEx;
        }

        Optional<TextAnswer> textAnswerOpt = textAnswerRepository.findById(rightAnswerId);
        if (textAnswerOpt.isEmpty()) {
            ExceptionWrapper serverError = new ExceptionWrapper(new RuntimeException("Correct answer data is corrupted."));
            serverError.addError("Answer", "The correct answer is not a TextAnswer or does not exist.");
            throw serverError;
        }

        return textAnswerOpt.get();
    }

    @Override
    @Transactional
    public void saveUserAnswer(String groupId, String playerId, String answerId) throws ExceptionWrapper {
        Optional<ChatGroup> groupOpt = groupRepository.findById(UUID.fromString(groupId));
        if (groupOpt.isEmpty()) {
            ExceptionWrapper badRequestEx = new ExceptionWrapper(new BadRequestException());
            badRequestEx.addError("Group", "Group not found");
            throw badRequestEx;
        }

        ChatGroup group = groupOpt.get();
        if (group.getUsersAnswers() == null) {
            group.setUsersAnswers(new java.util.HashMap<>());
        }
        
        group.getUsersAnswers().put(
            UUID.fromString(playerId),
            UUID.fromString(answerId)
        );
        
        groupRepository.save(group);
    }

    @Override
    public void sendAnswerResults(String groupId) throws ExceptionWrapper {
        Optional<ChatGroup> groupOpt = groupRepository.findById(UUID.fromString(groupId));
        if (groupOpt.isEmpty()) {
            ExceptionWrapper badRequestEx = new ExceptionWrapper(new BadRequestException());
            badRequestEx.addError("Group", "Group not found");
            throw badRequestEx;
        }

        ChatGroup group = groupOpt.get();
        ChoiceQuestion currentQuestion = (ChoiceQuestion) group.getCurrentQuestion();
        
        if (currentQuestion == null) {
            ExceptionWrapper badRequestEx = new ExceptionWrapper(new BadRequestException());
            badRequestEx.addError("Question", "No current question in the group");
            throw badRequestEx;
        }

        Map<UUID, UUID> usersAnswers = group.getUsersAnswers();
        TextAnswer correctAnswer = getCorrectAnswer(currentQuestion.getId());

        AnswerResultMessage answerResultMessage = new AnswerResultMessage();
        answerResultMessage.setCorrectAnswer(correctAnswer.getId());
        answerResultMessage.setCorrectAnswerText(correctAnswer.getText());
        answerResultMessage.setUsersAnswers(usersAnswers);

        _connectionService.sendMessageToGroup(UUID.fromString(groupId), answerResultMessage);
    }
}

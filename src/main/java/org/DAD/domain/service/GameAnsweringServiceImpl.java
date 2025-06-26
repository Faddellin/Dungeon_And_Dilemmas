package org.DAD.domain.service;

import lombok.AllArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.repository.AnswerRepository;
import org.DAD.application.repository.ChoiceQuestionRepository;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.application.repository.TextAnswerRepository;
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
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceQuestionRepository choiceQuestionRepository;
    private final TextAnswerRepository textAnswerRepository;
    private final GroupRepository groupRepository;
    private final SimpMessagingTemplate messagingTemplate;

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
    public String getCorrectAnswer(UUID questionId) throws ExceptionWrapper {
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

        return textAnswerOpt.get().getText();
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

        if (group.getUsersAnswers().size() == group.getMembers().size()) {
            sendAnswerResults(groupId);
        }
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
        String correctAnswer = getCorrectAnswer(currentQuestion.getId());
        
        for (User user : group.getMembers()) {
            AnswerResultMessage answerResultMessage = new AnswerResultMessage();
            answerResultMessage.setPlayerId(user.getId().toString());
            answerResultMessage.setCorrectAnswer(correctAnswer);
            
            UUID userAnswerId = usersAnswers != null ? usersAnswers.get(user.getId()) : null;
            if (userAnswerId != null) {
                answerResultMessage.setCorrect(isAnswerCorrect(currentQuestion.getId(), userAnswerId));
            } 
            else {
                answerResultMessage.setCorrect(false);
            }

            messagingTemplate.convertAndSend("/topic/game/" + groupId, answerResultMessage);
        }
    }
}

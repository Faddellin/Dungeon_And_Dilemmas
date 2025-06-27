package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Answer.AnswerCreateModel;
import org.DAD.application.model.Answer.TextAnswerCreateModel;
import org.DAD.application.model.CommonModels.PaginationModel;
import org.DAD.application.model.Question.QuestionCreateModel;
import org.DAD.application.model.Question.QuestionModel;
import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizDetailModel;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.model.Quiz.QuizModel;
import org.DAD.application.model.Quiz.QuizPagedListModel;
import org.DAD.application.repository.AnswerRepository;
import org.DAD.application.repository.QuestionRepository;
import org.DAD.application.repository.QuizRepository;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.service.QuizService;
import org.DAD.domain.entity.Answer.Answer;
import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.Quiz.QuizDifficulty;
import org.DAD.domain.entity.Quiz.QuizStatus;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.mapper.AnswerMapper;
import org.DAD.domain.mapper.QuestionMapper;
import org.DAD.domain.mapper.QuizMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository _quizRepository;
    private final QuestionRepository _questionRepository;
    private final AnswerRepository _answerRepository;
    private final UserRepository _userRepository;

    public QuizServiceImpl(
            QuizRepository quizRepository,
            UserRepository userRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository) {
        this._quizRepository = quizRepository;
        this._userRepository = userRepository;
        this._questionRepository = questionRepository;
        this._answerRepository = answerRepository;
    }

    @Transactional
    public UUID createQuiz(UUID userId, QuizCreateModel quizCreateModel) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);

        if(userO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("User", "User is not exists");
            throw entityNotFoundException;
        }

        User user = userO.get();


        Quiz quiz = QuizMapper.INSTANCE.quizCreateModelToQuiz(
                quizCreateModel,
                QuizDifficulty.Unknown,
                QuizStatus.Draft,user
        );

        _quizRepository.save(quiz);
        _quizRepository.flush();

        return quiz.getId();
    }

    @Transactional
    public void deleteQuiz(UUID userId, UUID quizId) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);
        Optional<Quiz> quizO = _quizRepository.findById(quizId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());

            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(quizO.isEmpty()){
                entityNotFoundException.addError("Quiz", "Quiz is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Quiz quiz = quizO.get();

        {
            if (!quiz.getCreator().equals(user)) {
                ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
                accessDeniedException.addError("Access", "You do not have permission to delete this quiz");
                throw accessDeniedException;
            }
        }

        quiz.getQuestions().forEach(question -> {

            switch (question.getQuestionType()){
                default -> {
                    ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;
                    _answerRepository.deleteAll(choiceQuestion.getAnswers());
                }
            }
        });
        _questionRepository.deleteAll(quiz.getQuestions());
        _quizRepository.delete(quiz);
        _quizRepository.flush();
        _questionRepository.flush();
        _answerRepository.flush();
    }

    public QuizModel getQuizById(UUID quizId) throws ExceptionWrapper{
        Optional<Quiz> quizO = _quizRepository.findById(quizId);

        if (quizO.isEmpty()) {
            ExceptionWrapper entityNotFound = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFound.addError("Quiz", "Quiz is not exists");
            throw entityNotFound;
        }

        return QuizMapper.INSTANCE.quizToQuizModel(quizO.get());

    }

    public QuizDetailModel getQuizDetailById(UUID userId, UUID quizId) throws ExceptionWrapper{
        Optional<Quiz> quizO = _quizRepository.findById(quizId);
        Optional<User> userO = _userRepository.findById(userId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(quizO.isEmpty()){
                entityNotFoundException.addError("Quiz", "Quiz is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Quiz quiz = quizO.get();

        // Проверяем, что пользователь является владельцем квиза
        if (!quiz.getCreator().equals(user)) {
            ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
            accessDeniedException.addError("Access", "You do not have permission to view detailed information of this quiz");
            throw accessDeniedException;
        }

        QuizDetailModel quizDetailModel = QuizMapper.INSTANCE.quizToQuizDetailModel(quiz);
        
        // Ручной маппинг вопросов с ответами
        if (quiz.getQuestions() != null) {
            List<QuestionModel> questionModels = quiz.getQuestions().stream()
                    .map(question -> {
                        QuestionModel questionModel = QuestionMapper.INSTANCE.questionToQuestionModel(question);
                        
                        // Для ChoiceQuestion маппим ответы
                        if (question instanceof ChoiceQuestion choiceQuestion && choiceQuestion.getAnswers() != null) {
                            questionModel.setAnswers(
                                choiceQuestion.getAnswers().stream()
                                    .map(answer -> {
                                        if (answer instanceof org.DAD.domain.entity.Answer.TextAnswer textAnswer) {
                                            return AnswerMapper.INSTANCE.textAnswerToTextAnswerModel(textAnswer);
                                        }
                                        return null;
                                    })
                                    .filter(answer -> answer != null)
                                    .collect(java.util.stream.Collectors.toList())
                            );
                        }
                        
                        return questionModel;
                    })
                    .sorted((q1, q2) -> Integer.compare(
                            q1.getQuestionNumber() != null ? q1.getQuestionNumber() : 0,
                            q2.getQuestionNumber() != null ? q2.getQuestionNumber() : 0
                    ))
                    .toList();
            quizDetailModel.setQuestions(questionModels);
        }
        
        return quizDetailModel;
    }

    public QuizPagedListModel getQuizzesByFilters(QuizFiltersModel quizFiltersModel) throws ExceptionWrapper{

        Integer quizzesCount = _quizRepository.getCountByFilters(quizFiltersModel);

        List<QuizModel> quizModels = _quizRepository.findByFilters(quizFiltersModel).stream()
                .map(QuizMapper.INSTANCE::quizToQuizModel)
                .toList();

        PaginationModel paginationModel = new PaginationModel(quizzesCount, quizFiltersModel.getPageSize(), quizFiltersModel.getPage());

        return new QuizPagedListModel(quizModels, paginationModel);
    }

    @Transactional
    public UUID createQuestion(UUID userId, UUID quizId, QuestionCreateModel questionCreateModel) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(userId);
        Optional<Quiz> quizO = _quizRepository.findById(quizId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());

            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(quizO.isEmpty()){
                entityNotFoundException.addError("Quiz", "Quiz is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Quiz quiz = quizO.get();

        {
            if (!quiz.getCreator().equals(user)) {
                ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
                accessDeniedException.addError("Access", "You do not have permission to create question for this quiz");
                throw accessDeniedException;
            }
        }

        int questionNumber = quiz.getQuestions() != null ? quiz.getQuestions().size() + 1 : 1;

        Question question = switch (questionCreateModel.getQuestionType()){
            default ->  QuestionMapper.INSTANCE.questionCreateModelToChoiceQuestion(
                        questionCreateModel,
                        quiz
                );
        };

        question.setQuestionNumber(questionNumber);

        _questionRepository.save(question);
        _questionRepository.flush();

        return question.getId();
    }

    @Transactional
    public void deleteQuestion(UUID userId, UUID questionId) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);
        Optional<Question> questionO = _questionRepository.findById(questionId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());

            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(questionO.isEmpty()){
                entityNotFoundException.addError("Question", "Question is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Question question = questionO.get();
        Quiz quiz = question.getQuiz();

        {
            if (!quiz.getCreator().equals(user)) {
                ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
                accessDeniedException.addError("Access", "You do not have permission to delete this question");
                throw accessDeniedException;
            }
        }

        int deletedQuestionNumber = question.getQuestionNumber();

        switch (question.getQuestionType()){
            default -> {
                ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;
                _answerRepository.deleteAll(choiceQuestion.getAnswers());
            }
        }

        _questionRepository.delete(question);
        _questionRepository.flush();
        _answerRepository.flush();

        List<Question> remainingQuestions = quiz.getQuestions().stream()
                .filter(q -> !q.getId().equals(questionId))
                .filter(q -> q.getQuestionNumber() > deletedQuestionNumber)
                .toList();

        for (Question q : remainingQuestions) {
            q.setQuestionNumber(q.getQuestionNumber() - 1);
            _questionRepository.save(q);
        }

        _questionRepository.flush();
    }

    @Transactional
    public UUID createAnswer(UUID userId, UUID questionId, AnswerCreateModel answerCreateModel) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(userId);
        Optional<Question> questionO = _questionRepository.findById(questionId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());

            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(questionO.isEmpty()){
                entityNotFoundException.addError("Question", "Question is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Question question = questionO.get();

        {
            if (!question.getQuiz().getCreator().equals(user)) {
                ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
                accessDeniedException.addError("Access", "You do not have permission to create answer for this question");
                throw accessDeniedException;
            }
        }

        Answer answer = switch (question.getQuestionType()){
            default ->  handleChoiceQuestionAnswerAdding((ChoiceQuestion) question, answerCreateModel);
        };

        _answerRepository.save(answer);
        _answerRepository.flush();

        return answer.getId();
    }

    @Transactional
    public void setCorrectAnswer(UUID userId, UUID questionId, UUID answerId) throws ExceptionWrapper{
        Optional<User> userO = _userRepository.findById(userId);
        Optional<Question> questionO = _questionRepository.findById(questionId);
        Optional<Answer> answerO = _answerRepository.findById(answerId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());

            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(questionO.isEmpty()){
                entityNotFoundException.addError("Question", "Question is not exists");
            }
            if(answerO.isEmpty()){
                entityNotFoundException.addError("Answer", "Answer is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Question question = questionO.get();

        {
            if (!question.getQuiz().getCreator().equals(user)) {
                ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
                accessDeniedException.addError("Access", "You do not have permission to set right answer for this question");
                throw accessDeniedException;
            }
        }

        if (question instanceof ChoiceQuestion choiceQuestion) {
            choiceQuestion.setRightAnswerId(answerId);
            _questionRepository.save(choiceQuestion);
            _questionRepository.flush();
        }
    }


    private Answer handleChoiceQuestionAnswerAdding(ChoiceQuestion choiceQuestion, AnswerCreateModel answerCreateModel) throws ExceptionWrapper {
        {
            ExceptionWrapper badRequestException = new ExceptionWrapper(new BadRequestException());
            if(choiceQuestion.getAnswers().size() == 4){
                badRequestException.addError("Invalid action", "This choice question already has 4 answers");
            }
            if(choiceQuestion.getAnswers().size() >= 1  && choiceQuestion.getAnswers().get(0).getAnswerType() != answerCreateModel.getAnswerType()){
                badRequestException.addError("Ivalid action", "Choice question cannot contain answers with different type");
            }
            if(badRequestException.hasErrors()){
                throw badRequestException;
            }
        }

        Answer answer = switch (answerCreateModel.getAnswerType()){
            default -> AnswerMapper.INSTANCE.textAnswerCreateModelToTextAnswer((TextAnswerCreateModel) answerCreateModel, choiceQuestion, false);
        };

        choiceQuestion.getAnswers().add(answer);

        return answer;
    }

    @Transactional
    public void deleteAnswer(UUID userId, UUID answerId) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findById(userId);
        Optional<Answer> answerO = _answerRepository.findById(answerId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());

            if(userO.isEmpty()){
                entityNotFoundException.addError("User", "User is not exists");
            }
            if(answerO.isEmpty()){
                entityNotFoundException.addError("Answer", "Answer is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        User user = userO.get();
        Answer answer = answerO.get();

        {
            if (!answer.getQuestion().getQuiz().getCreator().equals(user)) {
                ExceptionWrapper accessDeniedException = new ExceptionWrapper(new AccessDeniedException(""));
                accessDeniedException.addError("Access", "You do not have permission to delete this answer");
                throw accessDeniedException;
            }
        }

        _answerRepository.delete(answer);
        _answerRepository.flush();
    }

}

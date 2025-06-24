package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Answer.AnswerCreateModel;
import org.DAD.application.model.Answer.AnswerModel;
import org.DAD.application.model.Answer.TextAnswerModel;
import org.DAD.application.model.CommonModels.ResponseModel;
import org.DAD.application.model.Connection.ModelWrapper;
import org.DAD.application.model.Connection.UserAnswerModel;
import org.DAD.application.model.Question.QuestionCreateModel;
import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.model.Quiz.QuizModel;
import org.DAD.application.model.Quiz.QuizPagedListModel;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.service.QuizService;
import org.DAD.domain.entity.Answer.AnswerType;
import org.DAD.domain.entity.User.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class QuizController {

    private final QuizService _quizService;

    public QuizController(QuizService quizService) {
        _quizService = quizService;
    }

    @PostMapping(path = "quizzes/")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz has been created"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public UUID CreateQuiz(
            @Valid @RequestBody QuizCreateModel quizCreateModel
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _quizService.createQuiz(authentication.getId(),quizCreateModel);
    }
    @PostMapping(path = "check")
    @SecurityRequirement(name = "JWT")
    public void cfsfdz(
            @Valid @RequestBody ModelWrapper wrapper
    ) throws ExceptionWrapper {
        var a =5;
        var b =wrapper instanceof UserAnswerModel;
    }

    @DeleteMapping(path = "quizzes/{quizId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz has been deleted"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public void DeleteQuiz(
            UUID quizId
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        _quizService.deleteQuiz(authentication.getId(), quizId);
    }

    @GetMapping(path = "quizzes/{id}")
    public QuizModel GetQuizById(
            @PathVariable UUID id
    ) throws ExceptionWrapper {
        return _quizService.getQuizById(id);
    }

    @GetMapping(path = "quizzes")
    public QuizPagedListModel GetQuizzesByFilters(
            @ModelAttribute QuizFiltersModel quizFiltersModel
    ) throws ExceptionWrapper {
        return _quizService.getQuizzesByFilters(quizFiltersModel);
    }

    @PostMapping(path = "quizzes/{quizId}/questions")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create question for specific quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Question has been created"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public UUID CreateQuestion(
            @PathVariable UUID quizId,
            @Valid @RequestBody QuestionCreateModel questionCreateModel
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _quizService.createQuestion(authentication.getId(), quizId, questionCreateModel);
    }

    @DeleteMapping(path = "quizzes/questions/{questionId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Question has been deleted"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public void DeleteQuestion(
            @PathVariable UUID questionId
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        _quizService.deleteQuestion(authentication.getId(), questionId);
    }

    @PostMapping(path = "quizzes/questions/{questionId}/answers")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create answer for specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Answer has been created"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public UUID CreateAnswer(
            @PathVariable UUID questionId,
            @Valid @RequestBody AnswerCreateModel answerCreateModel
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _quizService.createAnswer(authentication.getId(), questionId, answerCreateModel);
    }

    @DeleteMapping(path = "quizzes/questions/answers/{answerId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete answer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Answer has been deleted"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public void DeleteAnswer(
            @PathVariable UUID answerId
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        _quizService.deleteAnswer(authentication.getId(), answerId);
    }

}

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
import org.DAD.application.model.CommonModels.ResponseModel;
import org.DAD.application.model.Question.QuestionCreateModel;
import org.DAD.application.model.Quiz.*;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.service.QuizService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping(path = "quizzes/{quizId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Publish quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz has been published"
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
    public void PublishQuiz(
            @PathVariable UUID quizId
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        _quizService.publishQuiz(authentication.getId(), quizId);
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get quiz by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz retrieved"
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
    public QuizModel GetQuizById(
            @PathVariable UUID id
    ) throws ExceptionWrapper {
        return _quizService.getQuizById(id);
    }

    @GetMapping(path = "quizzes/{quizId}/detail")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get detailed quiz by id (owner only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz detailed information retrieved"
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
            ),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - not quiz owner",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    public QuizDetailModel GetQuizDetailById(
            @PathVariable UUID quizId
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _quizService.getQuizDetailById(authentication.getId(), quizId);
    }

    @GetMapping(path = "quizzes")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get quizzes by filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "QuizPagedListModel retrieved"
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
    public QuizPagedListModel GetQuizzesByFilters(
            @ModelAttribute @Valid QuizFiltersModel quizFiltersModel
    ) throws ExceptionWrapper {
        return _quizService.getQuizzesByFilters(quizFiltersModel);
    }

    @GetMapping(path = "quizzes/user")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get user quizzes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User quizzes retrieved"
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
    public List<QuizModel> GetUserQuizzes() throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _quizService.getUserQuizzes(authentication.getId());
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

    @PutMapping(path = "quizzes/questions/{questionId}/answers/{answerId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Set correct answer for question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Correct answer has been set"
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
    public void SetCorrectAnswer(
            @PathVariable UUID questionId,
            @PathVariable UUID answerId
    ) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        _quizService.setCorrectAnswer(authentication.getId(), questionId, answerId);
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

package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Answer.AnswerCreateModel;
import org.DAD.application.model.CommonModels.ResponseModel;
import org.DAD.application.model.Connection.PlayerIsReadyMessage;
import org.DAD.application.model.Group.GroupModel;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.service.GroupService;
import org.DAD.application.service.QuizService;
import org.DAD.application.util.CodeGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService _groupService;

    public GroupController(GroupService groupService) {
        _groupService = groupService;
    }

    @PostMapping()
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Group has been created"
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
    public GroupModel CreateGroup() throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _groupService.createGroup(authentication.getId());
    }

    @PostMapping(path = "/{code}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Join group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Group has been joined"
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
    public GroupModel JoinGroup(@PathVariable @Valid @Pattern(regexp = "[0-9a-fA-F]{5}") String code) throws ExceptionWrapper {
        JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
        return _groupService.joinGroup(authentication.getId(), code);
    }

    @PostMapping(path = "/select-quiz/{quizId}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Select quiz for group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz has been selected"
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
    public GroupModel SelectQuiz(@PathVariable UUID quizId) throws ExceptionWrapper {
        return _groupService.selectQuiz(getCurrentUserId(), quizId);
    }

    private UUID getCurrentUserId() throws ExceptionWrapper {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication jwtAuth) {
            return jwtAuth.getId();
        } else {
            ExceptionWrapper authEx = new ExceptionWrapper(new AuthException());
            authEx.addError("Authentication", "Invalid authentication");
            throw authEx;
        }
    }
}

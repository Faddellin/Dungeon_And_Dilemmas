package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class GroupController {

    private final GroupService _groupService;

    public GroupController(GroupService groupService) {
        _groupService = groupService;
    }

    @GetMapping(path = "groups/")
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

    @GetMapping(path = "groups/test")
    public void test() throws ExceptionWrapper {
        _groupService.createGroup(UUID.fromString("89ce0044-8b6a-4126-b414-61bce247f9dd"));
        PlayerIsReadyMessage pirm = new PlayerIsReadyMessage();
        pirm.setPlayerId("89ce0044-8b6a-4126-b414-61bce247f9dd");
        pirm.setIsReady(true);
        _groupService.setPlayerIsReady(pirm);

    }

    @GetMapping(path = "groups/{code}")
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

}

package org.DAD.application.model.Quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.DAD.application.model.CommonModels.PaginationModel;
import org.DAD.application.model.User.UserModel;
import org.DAD.domain.entity.Quiz.QuizDifficulty;
import org.DAD.domain.entity.Quiz.QuizStatus;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class QuizPagedListModel {

    @NotNull
    private List<QuizModel> quizModels;

    @NotNull
    private PaginationModel paginationModel;

}

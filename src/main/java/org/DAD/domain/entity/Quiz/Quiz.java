package org.DAD.domain.entity.Quiz;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.entity.User.User;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Quiz {

    @Id
    private UUID id;

    @NotBlank
    @Min(1)
    @Max(64)
    private String title;

    private String description;

    @NotNull
    private QuizDifficulty difficulty;

    @NotNull
    private QuizStatus status;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}

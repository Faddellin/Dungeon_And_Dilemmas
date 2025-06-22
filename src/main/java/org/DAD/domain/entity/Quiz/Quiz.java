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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 64)
    private String title;

    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuizDifficulty difficulty;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuizStatus status;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}

package org.DAD.domain.entity.Result;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;

@Entity
public class Result {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @NotNull
    private Integer score;

}

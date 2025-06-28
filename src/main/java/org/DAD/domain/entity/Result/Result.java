package org.DAD.domain.entity.Result;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Result.ResultId.class)
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultId implements Serializable {
        private UUID user;
        private UUID quiz;
    }
}
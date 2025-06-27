package org.DAD.domain.entity.Group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.DAD.domain.entity.Question.Question;
import org.DAD.domain.entity.Quiz.Quiz;
import org.DAD.domain.entity.User.User;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
public class ChatGroup {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private UUID ownerId;

    @NotNull
    @Size(min = 5, max = 5)
    @Column(unique = true)
    private String code;

    @OneToMany(mappedBy = "currentGroup")
    @Size(min = 1, max = 4)
    private List<User> members;

    @ManyToOne
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;

    private Boolean acceptingAnswers;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<UUID, UUID> usersAnswers;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<UUID, Boolean> usersReady;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}

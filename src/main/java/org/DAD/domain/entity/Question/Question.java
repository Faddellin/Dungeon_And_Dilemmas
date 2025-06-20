package org.DAD.domain.entity.Question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.DAD.domain.entity.Answer.Answer;
import org.DAD.domain.entity.Quiz.Quiz;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "question_type")
//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "questionType"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = ChoiceQuestion.class, name = "choice"),
//})
public abstract class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull @Min(20) @Max(60)
    private Integer duration;
    //In seconds

    @NotNull @Min(1) @Max(30)
    private Integer damage;

    @NotNull @Min(1) @Max(100)
    private Integer reward;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", insertable=false, updatable=false)
    @NotNull
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}

package org.DAD.domain.entity.Question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
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
@Data
public abstract class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @NotNull @Min(20) @Max(60)
    protected Integer duration;
    //In seconds

    @NotNull @Min(1) @Max(30)
    protected Integer damage;

    @NotNull @Min(1) @Max(100)
    protected Integer reward;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", insertable=false, updatable=false)
    @NotNull
    protected QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    protected Quiz quiz;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 256)
    private String questionText;
}

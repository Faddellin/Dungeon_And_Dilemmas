package org.DAD.domain.entity.Answer;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Question.ChoiceQuestion;
import org.DAD.domain.entity.Question.Question;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "answer_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "answerType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextAnswer.class, name = "text")
})
@Data
public abstract class Answer {

    @Id
    @GeneratedValue
    protected UUID id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    protected Question question;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_type", insertable=false, updatable=false)
    @NotNull
    protected AnswerType answerType;

}

package org.DAD.domain.entity.Question;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.DAD.domain.entity.Answer.Answer;

import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("choice")
@Data
public class ChoiceQuestion
        extends Question {

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @Size(max = 4)
    private List<Answer> answers;

    private UUID rightAnswerId;

}

package org.DAD.domain.entity.Question;

import jakarta.persistence.*;
import org.DAD.domain.entity.Answer.Answer;

import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue("choice")
public class ChoiceQuestion
        extends Question {

    //@OneToMany(mappedBy = "question")
    //private List<Answer> answers;

    private UUID rightAnswerId;

}

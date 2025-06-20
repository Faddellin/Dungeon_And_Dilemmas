package org.DAD.domain.entity.Answer;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
@DiscriminatorValue("text")
public class TextAnswer
        extends Answer {

    @NotBlank
    private String text;

}

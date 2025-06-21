package org.DAD.domain.entity.Answer;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@DiscriminatorValue("text")
@Data
public class TextAnswer
        extends Answer {

    @NotBlank
    private String text;

}

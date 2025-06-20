package org.DAD.domain.entity.User;

import jakarta.persistence.*;
import org.DAD.domain.entity.Quiz.Quiz;

import java.util.List;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "creator")
    private List<Quiz> userQuizzes;

}

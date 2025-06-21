package org.DAD.domain.entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.DAD.domain.entity.Quiz.Quiz;

import java.util.List;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @NotNull
    public String email;
    @Enumerated(EnumType.STRING)
    @NotNull
    public UserRole role;
    @NotNull
    public String userName;
    public String refreshToken;
    public Date refreshTokenExpiryDate;
    @NotNull
    public String passwordHash;
    @OneToMany(mappedBy = "creator")
    private List<Quiz> userQuizzes;
}

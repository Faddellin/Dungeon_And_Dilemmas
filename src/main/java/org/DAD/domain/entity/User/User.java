package org.DAD.domain.entity.User;

import jakarta.persistence.*;

import java.util.List;
import java.util.Date;
import java.util.UUID;

@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "creator")
    private List<Quiz> userQuizzes;

    public UUID id;
    public String email;
    @Enumerated(EnumType.STRING)
    public UserRole role;
    public String userName;
    public String refreshToken;
    public Date refreshTokenExpiryDate;
    public String passwordHash;
}

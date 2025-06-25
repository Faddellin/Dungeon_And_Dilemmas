package org.DAD.domain.entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.Quiz.Quiz;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Email
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
    @ManyToOne
    @JoinColumn(name = "current_group_id")
    private ChatGroup currentGroup;
}

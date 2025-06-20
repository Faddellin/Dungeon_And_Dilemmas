package org.DAD.domain.entity.User;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    public UUID id;
    public String email;
    @Enumerated(EnumType.STRING)
    public UserRole role;
    public String userName;
    public String refreshToken;
    public Date refreshTokenExpiryDate;
    public String passwordHash;
}

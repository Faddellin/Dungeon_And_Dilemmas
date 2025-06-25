package org.DAD.domain.entity.Group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.DAD.domain.entity.User.User;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class ChatGroup {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Size(min = 5, max = 5)
    @Column(unique = true)
    private String code;

    @OneToMany(mappedBy = "currentGroup")
    @Size(min = 1, max = 4)
    private List<User> members;

}

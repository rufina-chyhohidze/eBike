package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "profile")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRoles userRoles;

    public User(String name, String email, String password, UserRoles userRoles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }
}

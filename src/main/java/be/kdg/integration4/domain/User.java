package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
abstract class User {
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

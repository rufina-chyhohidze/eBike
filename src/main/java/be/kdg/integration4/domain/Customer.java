package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class Customer extends User {
    private String phoneNumber;

    public Customer(String name, String email, String password, UserRoles userRoles, String phoneNumber) {
        super(name, email, password, userRoles);
        this.phoneNumber = phoneNumber;
    }
}

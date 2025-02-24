package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class SystemAdmin extends User {

    public SystemAdmin(String name, String email, String password, UserRoles userRoles) {
        super(name, email, password, userRoles);
    }
}

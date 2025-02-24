package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class Technician extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    private Workshop workshop;


    public Technician(String name, String email, String password, UserRoles userRoles, Workshop workshop) {
        super(name, email, password, userRoles);
        this.workshop = workshop;
    }
}

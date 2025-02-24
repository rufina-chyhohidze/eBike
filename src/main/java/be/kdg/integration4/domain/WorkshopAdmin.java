package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class WorkshopAdmin extends User {
    @OneToOne
    private Workshop workshop;
    private boolean approved;

//    public WorkshopAdmin(Workshop workshop) {
//        this.workshop = workshop;
//    }

    public WorkshopAdmin(String name, String email, String password, UserRoles userRoles, Workshop workshop,boolean approved) {
        super(name, email, password, userRoles);
        this.workshop = workshop;
        this.approved = approved;
    }
}

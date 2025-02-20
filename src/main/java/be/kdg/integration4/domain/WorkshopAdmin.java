package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class WorkshopAdmin extends User {
    @OneToOne
    private Workshop workshop;

    public WorkshopAdmin(Workshop workshop) {
        this.workshop = workshop;
    }
}

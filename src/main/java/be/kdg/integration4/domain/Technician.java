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

    @OneToMany(fetch = FetchType.LAZY)
    private List<BikeReport> bikeReports;

    @ManyToOne(fetch = FetchType.LAZY)
    private Workshop workshop;

    private boolean approved;


    public Technician(String name, String email, String password, UserRoles userRoles, List<BikeReport> bikeReports, Workshop workshop, boolean approved) {
        super(name, email, password, userRoles);
        this.bikeReports = bikeReports;
        this.workshop = workshop;
        this.approved = approved;
    }
}

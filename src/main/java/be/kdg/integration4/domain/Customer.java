package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class Customer extends User{
    private int phoneNumber;
    private List<BikeReport> bikeReports;

    public Customer(int phoneNumber, List<BikeReport> bikeReports) {
        this.phoneNumber = phoneNumber;
        this.bikeReports = bikeReports;
    }

    public Customer(String name, String email, String password, UserRoles userRoles, int phoneNumber, List<BikeReport> bikeReports) {
        super(name, email, password, userRoles);
        this.phoneNumber = phoneNumber;
        this.bikeReports = bikeReports;
    }
}

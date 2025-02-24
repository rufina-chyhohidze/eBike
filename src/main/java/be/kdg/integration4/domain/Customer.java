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
public class Customer extends User {
    private Integer phoneNumber;
    @OneToMany(fetch = FetchType.LAZY)
    private List<BikeReport> bikeReports;

//    public Customer(int phoneNumber, List<BikeReport> bikeReports) {
//        this.phoneNumber = phoneNumber;
//        this.bikeReports = bikeReports;
//    }

    public Customer(String name, String email, String password, UserRoles userRoles, int phoneNumber, List<BikeReport> bikeReports) {
        super(name, email, password, userRoles);
        this.phoneNumber = phoneNumber;
        this.bikeReports = bikeReports;
    }
}

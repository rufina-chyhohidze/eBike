package be.kdg.integration4.domain.profile;

import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.Workshop;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("Customer")
public class Customer extends User {
    private String phoneNumber;
    @ManyToOne
    private Workshop registeredIn;

    @OneToMany(mappedBy = "bikeOwner", orphanRemoval = true)
    private List<Bike> bikes;

    public Customer(String name, String email, String password, String phoneNumber, Workshop registeredIn) {
        super(name, email, password, true);
        this.phoneNumber = phoneNumber;
        this.registeredIn = registeredIn;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

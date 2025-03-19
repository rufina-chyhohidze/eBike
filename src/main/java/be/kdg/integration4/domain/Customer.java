package be.kdg.integration4.domain;

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

    @OneToMany(mappedBy = "bikeOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bike> bikes;

    public Customer(String name, String email, String password, String phoneNumber) {
        super(name, email, password, true);
        this.phoneNumber = phoneNumber;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }


    @Override
    public String toString() {
        return "Customer{" +
                "phoneNumber='" + phoneNumber + '\'' +
//                ", bikes=" + bikes.size() +
                '}';
    }
}

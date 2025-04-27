package be.kdg.integration4.domain.profile;

import be.kdg.integration4.domain.report.Workshop;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@ToString
@DiscriminatorValue("Technician")
public class Technician extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    private Workshop workshop;

    public Technician(String name, String email, String password, Workshop workshop) {
        super(name, email, password);
        this.workshop = workshop;
    }
}

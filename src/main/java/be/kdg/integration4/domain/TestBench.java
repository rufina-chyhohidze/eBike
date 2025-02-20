package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class TestBench {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer benchId;

    @OneToOne
    private Workshop workshop;


    public TestBench(Workshop workshop) {
        this.workshop = workshop;
    }
}

package be.kdg.integration4.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class BikeReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Bike bike;

    private LocalDate reportDate;

    private int score;

    @OneToMany(fetch = FetchType.LAZY)
    private List<TestLine> testLines;

}

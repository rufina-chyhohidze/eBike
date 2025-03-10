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

    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "testline_id")
    private List<TestLine> testLines;

    public BikeReport(Bike bike, LocalDate reportDate, Integer score, Technician technician, Customer customer, List<TestLine> testLines) {
        this.bike = bike;
        this.reportDate = reportDate;
        this.score = score;
        this.technician = technician;
        this.customer = customer;
        this.testLines = testLines;
    }

    public BikeReport(Bike bike, LocalDate reportDate, Technician technician, Customer customer, List<TestLine> testLines) {
        this.bike = bike;
        this.reportDate = reportDate;
        this.technician = technician;
        this.customer = customer;
        this.testLines = testLines;
    }
}




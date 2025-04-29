package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bench_nr")
    private TestBench testBench;

    private String testId;

    public BikeReport(Long id, String bike, String reportDate, Integer score, String technician, String customer) {
    }

    public BikeReport(Bike bike, LocalDate reportDate, Technician technician, Customer customer, TestBench testBench) {
        this.bike = bike;
        this.reportDate = reportDate;
        this.technician = technician;
        this.customer = customer;
        this.testBench = testBench;
    }
}

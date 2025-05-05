package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.enums.FunctionalTestComponents;
import be.kdg.integration4.domain.enums.InspectionCondition;
import be.kdg.integration4.domain.enums.VisualInspectionComponents;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @JoinColumn(name = "bikereport_id")
    private List<TestLine> testLines;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bench_nr")
    private TestBench testBench;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "visual_inspections", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "part")
    @Column(name = "condition")
    private Map<VisualInspectionComponents, InspectionCondition> visualInspection;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "functional_test", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "part")
    @Column(name = "condition")
    private Map<FunctionalTestComponents, InspectionCondition> functionalTest;

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

    public BikeReport(Bike bike, LocalDate now, Technician byEmail, Customer byEmail1, TestBench referenceById, Map<VisualInspectionComponents, InspectionCondition> inspection, Map<FunctionalTestComponents, InspectionCondition> functionalTest) {
        this.bike = bike;
        this.reportDate = now;
        this.technician = byEmail;
        this.customer = byEmail1;
        this.testBench = referenceById;
        this.visualInspection = inspection;
        this.functionalTest = functionalTest;
    }




}

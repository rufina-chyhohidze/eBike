package be.kdg.integration4.domain.report;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ApiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private BikeReport report;

    private String testId;

    public ApiRequest(BikeReport report, String testId) {
        this.report = report;
        this.testId = testId;
    }
}

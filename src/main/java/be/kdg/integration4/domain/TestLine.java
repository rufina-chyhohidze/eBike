package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class TestLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateTime;
    private int batteryVoltage;

    public TestLine(LocalDate dateTime, int batteryVoltage) {
        this.dateTime = dateTime;
        this.batteryVoltage = batteryVoltage;
    }
}

package be.kdg.integration4.service.dtos;

public class BatteryTestDTO {
    private Double availableCapacity;  // Beschikbare capaciteit (in Wh)
    private Double batteryHealth;      // Gezondheid batterij (in %)
    private Integer score;             // Score (rounded battery health)

    // Units as separate fields
    private String availableCapacityUnit = "Wh";
    private String batteryHealthUnit = "%";
    private String scoreUnit = "%";

    // Constructor, getters, and setters
    public BatteryTestDTO(Double availableCapacity, Double batteryHealth, Integer score) {
        this.availableCapacity = availableCapacity;
        this.batteryHealth = batteryHealth;
        this.score = score;
    }

    public Double getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Double availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public Double getBatteryHealth() {
        return batteryHealth;
    }

    public void setBatteryHealth(Double batteryHealth) {
        this.batteryHealth = batteryHealth;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    // Getter methods for units
    public String getAvailableCapacityUnit() {
        return availableCapacityUnit;
    }

    public String getBatteryHealthUnit() {
        return batteryHealthUnit;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }
}

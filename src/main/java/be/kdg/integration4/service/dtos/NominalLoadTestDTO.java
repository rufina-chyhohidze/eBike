package be.kdg.integration4.service.dtos;

public class NominalLoadTestDTO {
    private Double continuousLoad;    // Continue belasting
    private Double temperatureIncrease; // Temperatuurstijging
    private Double score;             // Score (based on temp increase)

    // Units
    private String continuousLoadUnit = "W";     // Unit for continuous load (Watts)
    private String temperatureIncreaseUnit = "°C";  // Unit for temperature increase (Celsius)
    private String scoreUnit = "%";               // Unit for score (Percentage)

    // Constructors
    public NominalLoadTestDTO(Double continuousLoad, Double temperatureIncrease, Double score) {
        this.continuousLoad = continuousLoad;
        this.temperatureIncrease = temperatureIncrease;
        this.score = score;
    }

    // Getters and setters for the values
    public Double getContinuousLoad() {
        return continuousLoad;
    }

    public void setContinuousLoad(Double continuousLoad) {
        this.continuousLoad = continuousLoad;
    }

    public Double getTemperatureIncrease() {
        return temperatureIncrease;
    }

    public void setTemperatureIncrease(Double temperatureIncrease) {
        this.temperatureIncrease = temperatureIncrease;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    // Getters for the units
    public String getContinuousLoadUnit() {
        return continuousLoadUnit;
    }

    public String getTemperatureIncreaseUnit() {
        return temperatureIncreaseUnit;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }
}

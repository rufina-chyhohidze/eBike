package be.kdg.integration4.service.dtos;

public class BearingHealthDTO {
    private boolean bearingStatus;  // true for "Good", false for "Bad"

    public BearingHealthDTO(boolean bearingStatus) {
        this.bearingStatus = bearingStatus;
    }

    public boolean isBearingStatus() {
        return bearingStatus;
    }

    public void setBearingStatus(boolean bearingStatus) {
        this.bearingStatus = bearingStatus;
    }
}

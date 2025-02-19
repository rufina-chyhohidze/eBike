package be.kdg.integration4.domain;

import java.time.LocalDate;

public class TestLine {
    private LocalDate dateTime;
    private int batteryVoltage;

    public TestLine(LocalDate dateTime, int batteryVoltage) {
        this.dateTime = dateTime;
        this.batteryVoltage = batteryVoltage;
    }

    public LocalDate getDateTime() {return dateTime;}
    public void setDateTime(LocalDate dateTime) {this.dateTime = dateTime;}
    public int getBatteryVoltage() {return batteryVoltage;}
    public void setBatteryVoltage(int batteryVoltage) {this.batteryVoltage = batteryVoltage;}

    @Override
    public String toString() {
        return "TestLine{" +
                "dateTime=" + dateTime +
                ", batteryVoltage=" + batteryVoltage +
                '}';
    }
}

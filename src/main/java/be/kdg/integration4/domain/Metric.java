package be.kdg.integration4.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Metric {
    BATTERY_VOLTAGE("Battery Voltage (V)", "batteryVoltage", "V"),
    BATTERY_CURRENT("Battery Current (A)", "batteryCurrent", "A"),
    BATTERY_CAPACITY("Battery Capacity (Wh)", "batteryCapacity", "Wh"),
    BATTERY_TEMPERATURE("Battery Temperature (°C)", "batteryTemperature", "°C"),
    CHARGE_STATUS("Charge Status", "chargeStatus", ""),
    ASSISTANCE_LEVEL("Assistance Level", "assistanceLevel", ""),
    TORQUE_CRANK("Torque Crank (Nm)", "torqueCrank", "Nm"),
    BIKE_WHEEL_SPEED("Bike Wheel Speed (km/h)", "bikeWheelSpeed", "km/h"),
    CADENCE("Cadence (RPM)", "cadence", "RPM"),
    ENGINE_RPM("Engine RPM", "engineRpm", "RPM"),
    ENGINE_POWER("Engine Power (W)", "enginePower", "W"),
    WHEEL_POWER("Wheel Power (W)", "wheelPower", "W"),
    ROLL_TORQUE("Roll Torque (Nm)", "rolTroque", "Nm"),
    LOADCELL("Loadcell (N)", "loadCell", "N"),
    ROLL("Roll", "rol", ""),
    HORIZONTAL_INCLINATION("Horizontal Inclination (°)", "horizontalInclinationSensor", "°"),
    VERTICAL_INCLINATION("Vertical Inclination (°)", "verticalInclinationSensor", "°"),
    LOAD_POWER("Load Power (W)", "loadPower", "W"),
    STATUS_PLUG("Status Plug", "statusPlug", "");

    private final String name;
    private final String camelCaseName;
    private final String metric;

    Metric(String name, String camelCaseName, String metric) {
        this.name = name;
        this.camelCaseName = camelCaseName;
        this.metric = metric;
    }

    public String getName() {
        return name;
    }

    public String getCamelCaseName() {
        return camelCaseName;
    }

    public String getMetric() {
        return metric;
    }

    public static Map<String, String> getMetricNamesMap() {
        Map<String, String> map = new HashMap<>();
        for (Metric metric : Metric.values()) {
            map.put(metric.getName(), metric.getCamelCaseName());
        }
        return map;
    }

    public static Map<String, String> getMetricsMap() {
        Map<String, String> map = new HashMap<>();
        for (Metric metric : Metric.values()) {
            map.put(metric.getName(), metric.getMetric());
        }
        return map;
    }
    }
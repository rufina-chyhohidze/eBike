package be.kdg.integration4.domain.enums;

public enum InspectionCondition {
    VERY_BAD("- -"),
    BAD("-"),
    NEUTRAL("/"),
    GOOD("+"),
    VERY_GOOD("++"),
    NOT_APPLICABLE("n.v.t");

    private final String label;

    InspectionCondition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}

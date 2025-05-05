package be.kdg.integration4.domain.enums;

public enum FunctionalTestComponents {
    TAIL_LIGHT,
    TAIL_BRAKE /*,
    DERAILLEUR_GEAR,
    DISPLAY,
    PHONE_HOLDER,
    HUB_ADJUSTMENT,
    FRONT_LIGHT,
    FRONT_BRAKE,
    SADDLE_SPRING
    */;

    @Override
    public String toString() {
        String[] parts = name().toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }
        return sb.toString().trim();
    }






}

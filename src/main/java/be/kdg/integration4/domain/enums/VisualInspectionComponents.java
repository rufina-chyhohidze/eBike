package be.kdg.integration4.domain.enums;

public enum VisualInspectionComponents {
    TIRES,
    BELL ,
    CRANKS,
    ELECTRICAL_WIRING/*,
    FRAME_FRONT_FORK,
    GRIPS,
    CHAIN_BELT,
    PEDALS,
    REFLECTORS,
    BRAKE_PADS,
    BRAKE_LEVERS,
    BRAKE_CABLES,
    BRAKE_DISCS,
    SHIFT_CABLES,
    FENDERS,
    HANDLEBARS,
    REAR_SPROCKET,
    FRONT_SPROCKET,
    RIMS_SPOKES,
    REAR_SUSPENSION,
    FRONT_SUSPENSION,
    SADDLE
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

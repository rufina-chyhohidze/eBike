package be.kdg.integration4.domain;

public class Workshop {
    private int workshopId;
    private String workshopName;
    private String workshopLocation;

    public Workshop() {
    }

    public Workshop(int workshopId, String workshopName, String workshopLocation) {
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.workshopLocation = workshopLocation;
    }

    public int getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(int workshopId) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopLocation() {
        return workshopLocation;
    }

    public void setWorkshopLocation(String workshopLocation) {
        this.workshopLocation = workshopLocation;
    }

    @Override
    public String toString() {
        return "Workshop{" +
                "workshopId=" + workshopId +
                ", workshopName='" + workshopName + '\'' +
                ", workshopLocation='" + workshopLocation + '\'' +
                '}';
    }
}

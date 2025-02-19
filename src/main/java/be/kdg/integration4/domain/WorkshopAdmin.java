package be.kdg.integration4.domain;

public class WorkshopAdmin {
    private Workshop workshop;

    public WorkshopAdmin() {
    }

    public WorkshopAdmin(Workshop workshop) {
        this.workshop = workshop;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }

    @Override
    public String toString() {
        return "WorkshopAdmin{" +
                "workshop=" + workshop +
                '}';
    }
}

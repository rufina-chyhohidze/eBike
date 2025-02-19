package be.kdg.integration4.domain;

import java.util.List;

public class Technician extends User {
    private List<BikeReport> bikeReports;
    private Workshop workshop;

    public Technician(List<BikeReport> bikeReports, Workshop workshop) {
        this.bikeReports = bikeReports;
        this.workshop = workshop;
    }

    public Technician(int id, String name, String email, String password, UserRoles userRoles, List<BikeReport> bikeReports, Workshop workshop) {
        super(id, name, email, password, userRoles);
        this.bikeReports = bikeReports;
        this.workshop = workshop;
    }

    public List<BikeReport> getBikeReports() {return bikeReports;}
    public void setBikeReports(List<BikeReport> bikeReports) {this.bikeReports = bikeReports;}
    public Workshop getWorkshop() {return workshop;}
    public void setWorkshop(Workshop workshop) {this.workshop = workshop;}

    @Override
    public String toString() {
        return "Technician{" +
                "bikeReports=" + bikeReports +
                ", workshop=" + workshop +
                '}';
    }
}

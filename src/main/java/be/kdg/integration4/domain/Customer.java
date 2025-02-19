package be.kdg.integration4.domain;

import java.util.List;

public class Customer extends User {
    private int phoneNumber;
    private List<BikeReport> bikeReports;

    public Customer(int phoneNumber, List<BikeReport> bikeReports) {
        this.phoneNumber = phoneNumber;
        this.bikeReports = bikeReports;
    }

    public Customer(int id, String name, String email, String password, UserRoles userRoles, int phoneNumber, List<BikeReport> bikeReports) {
        super(id, name, email, password, userRoles);
        this.phoneNumber = phoneNumber;
        this.bikeReports = bikeReports;
    }

    public int getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(int phoneNumber) {this.phoneNumber = phoneNumber;}
    public List<BikeReport> getBikeReports() {return bikeReports;}
    public void setBikeReports(List<BikeReport> bikeReports) {this.bikeReports = bikeReports;}

    @Override
    public String toString() {
        return "Customer{" +
                "phoneNumber=" + phoneNumber +
                ", bikeReports=" + bikeReports +
                '}';
    }
}

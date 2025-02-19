package be.kdg.integration4.domain;

public class TestBench {
    private int benchId;
    private Workshop workshop;

    public TestBench() {
    }

    public TestBench(int benchId, Workshop workshop) {
        this.benchId = benchId;
        this.workshop = workshop;
    }

    public int getBenchId() {
        return benchId;
    }

    public void setBenchId(int benchId) {
        this.benchId = benchId;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }

    @Override
    public String toString() {
        return "TestBench{" +
                "benchId=" + benchId +
                ", workshop=" + workshop +
                '}';
    }
}

package be.kdg.integration4.service;

import be.kdg.integration4.domain.BikeReport;
import be.kdg.integration4.repository.BikeReportRepository;

import java.util.List;

public interface BikeReportService {

    BikeReport findById(Long id);

    List<BikeReport> findAll();

    void save(Long id, String bike, String reportDate, Integer score, String technician, String customer);

    void delete(Long id);

    void save(Long testbenchNumber, String testType, String emailBikeOwner, String chassisNumber);
}

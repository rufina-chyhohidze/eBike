package be.kdg.integration4.service;

import be.kdg.integration4.domain.*;
import be.kdg.integration4.repository.BikeReportRepository;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BikeReportService {

    BikeReport findById(Long id);

    List<BikeReport> findAll();

    void save(Bike bike, LocalDate reportDate, Integer score, Technician technician, Customer customer, List<TestLine> testLines);

    void delete(Long id);

    List<TestLine> csvConverter(MultipartFile file, BikeReport bikeReport);

    Map<String, Double> calculateAverages(List<TestLine> testLines);
}

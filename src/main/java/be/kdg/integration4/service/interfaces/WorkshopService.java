package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.report.Workshop;

import java.util.List;


public interface WorkshopService {
    List<Workshop> getAll();
    Workshop getById(Long id);
}

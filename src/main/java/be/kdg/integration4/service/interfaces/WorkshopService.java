package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.Workshop;

import java.util.List;


public interface WorkshopService {
    List<Workshop> findAll();
    Workshop findById(Long id);
}

package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.repository.WorkshopRepository;
import be.kdg.integration4.service.interfaces.WorkshopService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;

    public WorkshopServiceImpl(WorkshopRepository workshopRepository) {
        this.workshopRepository = workshopRepository;
    }

    @Override
    public List<Workshop> findAll() {
        return this.workshopRepository.findAll();
    }

    @Override
    public Workshop findById(Long id) {
        return this.workshopRepository.findById(id).orElse(null);
    }
}

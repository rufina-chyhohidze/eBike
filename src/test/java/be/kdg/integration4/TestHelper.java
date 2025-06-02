package be.kdg.integration4;

import be.kdg.integration4.domain.enums.Location;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.WorkshopAdmin;
import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.repository.*;
import be.kdg.integration4.service.interfaces.CustomerService;
import be.kdg.integration4.service.interfaces.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {

    @Autowired
    public BikeReportRepository bikeReportRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public BikeRepository bikeRepository;
    @Autowired
    public CustomerRepository customerRepository;
    @Autowired
    public SystemAdminRepository systemAdminRepository;
    @Autowired
    public TechnicianRepository technicianRepository;
    @Autowired
    public TestBenchRepository testBenchRepository;
    @Autowired
    public TestLineRepository testLineRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public WorkshopRepository workshopRepository;
    @Autowired
    public WorkshopAdminRepository workshopAdminRepository;
    @Autowired
    public CustomerService customerService;
    @Autowired
    public ReportSettingRepository reportSettingRepository;

    public User createTechnician(String name, String email, String password, Workshop workshop) {
        return this.technicianRepository.save(
                new Technician(
                        name,
                        email,
                        password,
                        workshop
                )
        );
    }

    public Workshop createWorkshop(String workshopName, Location workshopLocation) {
        return this.workshopRepository.save(
                new Workshop(
                        workshopName,
                        workshopLocation
                )
        );
    }


    public User createCustomer(String name, String email, String phoneNumber, Long technicianId) {
        Customer customer = new Customer(name,email, passwordEncoder.encode("password"), phoneNumber,technicianRepository.findById(technicianId).orElseThrow().getWorkshop());
        return this.customerRepository.save(customer);

//        var result = this.registrationService.createCustomer(
//                name,
//                email,
//                phoneNumber,
//                technicianId
//        );
//        return result.customer();
    }

    public User createWorkshopAdmin(String name, String email, String password, Workshop workshop) {
        return this.workshopAdminRepository.save( new WorkshopAdmin(
                name,
                email,
                password,
                workshop
                )
        );
    }

    public void cleanUp() {
        bikeReportRepository.deleteAll();
        reportSettingRepository.deleteAll();
        bikeRepository.deleteAll();
        customerRepository.deleteAll();
        systemAdminRepository.deleteAll();
        technicianRepository.deleteAll();
        testBenchRepository.deleteAll();
        testLineRepository.deleteAll();
        userRepository.deleteAll();
        workshopRepository.deleteAll();
        workshopAdminRepository.deleteAll();
    }
}

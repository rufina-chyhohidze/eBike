package be.kdg.integration4.service.implementations;

import be.kdg.integration4.TestHelper;
import be.kdg.integration4.config.DotenvInitializer;
import be.kdg.integration4.domain.enums.Location;
import be.kdg.integration4.domain.enums.UserRole;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.exception.UserAlreadyExistsException;
import be.kdg.integration4.repository.CustomerRepository;
import be.kdg.integration4.repository.UserRepository;
import be.kdg.integration4.service.dtos.CustomerAndPasswordServiceDto;
import be.kdg.integration4.service.interfaces.RegistrationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(initializers = DotenvInitializer.class)
@SpringBootTest
@ActiveProfiles("test")
class RegistrationServiceTest {

    @Autowired
    private RegistrationService sut;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestHelper testHelper;

    private Workshop workshop;

    @BeforeEach
    void setUp() {
        this.workshop = testHelper.createWorkshop("Test1", Location.ANTWERP);
    }

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void createCustomerShouldAddCustomerToDatabase() {
        CustomerAndPasswordServiceDto dto = sut.createCustomer("testName", "test@test.com", "+3233333333");
        Customer dbCustomer = customerRepository.findByEmail("test@test.com").orElseThrow();
        assertEquals(dto.customer().getEmail(), dbCustomer.getEmail());
        assertEquals(dto.customer().getId(), dbCustomer.getId());
        assertEquals(dto.customer().getName(), dbCustomer.getName());
        assertEquals(dto.customer().getPhoneNumber(), dbCustomer.getPhoneNumber());
        assertEquals(dto.customer().getPassword(), dbCustomer.getPassword());
    }

    @Test
    void createCustomerShouldntAddCustomerIfCustomerWithThisEmailAlreadyExists() {
        testHelper.createCustomer("test", "test@test.com", "pass", "+3233333333");
        assertThrows(UserAlreadyExistsException.class, () -> sut.createCustomer("test", "test@test.com", "+3233333333"));
        assertThrows(UserAlreadyExistsException.class, () -> sut.createCustomer("differentName", "test@test.com", "+3233333333"));
        assertThrows(UserAlreadyExistsException.class, () -> sut.createCustomer("diffName", "test@test.com", "+3255555555"));
    }

    @Test
    void createStaffShouldAddTechnicianToDatabase() {
        User user = sut.createStaff("Technician", "technician@test.com", "qwerty", UserRole.TECHNICIAN.toString(), this.workshop.getWorkshopId());
        User userFromDb = testHelper.userRepository.findByEmail("technician@test.com").orElseThrow();
        assertEquals(user.getId(), userFromDb.getId());
        assertEquals(user.getName(), userFromDb.getName());
        assertEquals(user.getEmail(), userFromDb.getEmail());
        assertEquals(user.getPassword(), userFromDb.getPassword());
    }

    @Test
    void createStaffShouldAddWorkshopAdminToDatabase() {
        User user = sut.createStaff("WorkshopAdmin", "workshop@test.com", "qwerty", UserRole.WORSHOPADMIN.toString(), this.workshop.getWorkshopId());
        User userFromDb = testHelper.userRepository.findByEmail("workshop@test.com").orElseThrow();
        assertEquals(user.getId(), userFromDb.getId());
        assertEquals(user.getName(), userFromDb.getName());
        assertEquals(user.getEmail(), userFromDb.getEmail());
        assertEquals(user.getPassword(), userFromDb.getPassword());
    }

    @Test
    void createStaffShouldntAddStaffIfStaffWithThisEmailAlreadyExists() {
        testHelper.createTechnician("testTechnician", "technician@test.com","qwerty", this.workshop);
        testHelper.createWorkshopAdmin("testWorkshop", "workshop@test.com", "qwerty", this.workshop);
        assertThrows(UserAlreadyExistsException.class, () -> sut.createStaff("test", "technician@test.com", "pass", UserRole.TECHNICIAN.toString(), this.workshop.getWorkshopId()));
        assertThrows(UserAlreadyExistsException.class, () -> sut.createStaff("differentName", "technician@test.com", "pass", UserRole.TECHNICIAN.toString(), this.workshop.getWorkshopId()));
        assertDoesNotThrow(() -> sut.createStaff("diffName", "tech@test.com", "pass", UserRole.TECHNICIAN.toString(), this.workshop.getWorkshopId()));
        assertThrows(UserAlreadyExistsException.class, () -> sut.createStaff("workshopAdmin", "workshop@test.com", "qwerty", UserRole.WORSHOPADMIN.toString(), this.workshop.getWorkshopId()));
        assertThrows(UserAlreadyExistsException.class, () -> sut.createStaff("technicianWithWorkshopAdminEmail", "workshop@test.com", "qwerty", UserRole.TECHNICIAN.toString(), this.workshop.getWorkshopId()));
    }
}
package be.kdg.integration4.service.implementations;

import be.kdg.integration4.TestHelper;
import be.kdg.integration4.config.DotenvInitializer;
import be.kdg.integration4.domain.enums.Location;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.report.Workshop;
import be.kdg.integration4.repository.UserRepository;
import be.kdg.integration4.service.interfaces.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(initializers = DotenvInitializer.class)
@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserService sut;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestHelper testHelper;

    private User user;

    @BeforeEach
    void setUp() {
        Workshop workshop = this.testHelper.createWorkshop("Workshops1", Location.ANTWERP);
        user = testHelper.createTechnician("Test User", "test@example.com", "pass", workshop);
    }
    @AfterEach
    void cleanUp() {
        this.testHelper.cleanUp();
    }

    @Test
    void getUnapprovedUsers_shouldReturnUnapprovedUsers() {
        // Arrange:

        // Act
        List<User> unapproved = sut.getUnapprovedUsers();

        // Assert
        assertEquals(1, unapproved.size());
        assertFalse(unapproved.get(0).isApproved());
    }

    @Test
    void approveUser_shouldSetApprovedTrue() {
        // Act
        sut.approveUser(user.getId());

        // Assert
        User approvedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(approvedUser.isApproved());
    }

    @Test
    void rejectUser_shouldDeleteUser() {
        // Act
        sut.rejectUser(user.getId());

        // Assert
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        // Act
        User found = sut.getUserByEmail("test@example.com");

        // Assert
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    void getUserById_shouldReturnUser() {
        // Act
        User found = sut.getUserById(user.getId());

        // Assert
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }
}

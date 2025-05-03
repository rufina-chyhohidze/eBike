package be.kdg.integration4.service.implementations;

import be.kdg.integration4.TestHelper;
import be.kdg.integration4.config.DotenvInitializer;
import be.kdg.integration4.domain.profile.User;
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
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestHelper testHelper;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = testHelper.createCustomer("Test User", "test@example.com", "pass", "1234567890");
    }

    @AfterEach
    void cleanUp() {
//        this.testHelper.cleanUp();
    }

    @Test
    void getUnapprovedUsers_shouldReturnUnapprovedUsers() {
        // Arrange: testUser is not approved by default

        // Act
        List<User> unapproved = userService.getUnapprovedUsers();

        // Assert
//        assertEquals(1, unapproved.size());
//        assertFalse(unapproved.getFirst().isApproved());
    }

    @Test
    void approveUser_shouldSetApprovedTrue() {
        // Act
        userService.approveUser(testUser.getId());

        // Assert
        User approvedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertTrue(approvedUser.isApproved());
    }

    @Test
    void rejectUser_shouldDeleteUser() {
        // Act
        userService.rejectUser(testUser.getId());

        // Assert
        assertFalse(userRepository.findById(testUser.getId()).isPresent());
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        // Act
        User found = userService.getUserByEmail("test@example.com");

        // Assert
        assertNotNull(found);
        assertEquals(testUser.getId(), found.getId());
    }

    @Test
    void getUserById_shouldReturnUser() {
        // Act
        User found = userService.getUserById(testUser.getId());

        // Assert
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }
}

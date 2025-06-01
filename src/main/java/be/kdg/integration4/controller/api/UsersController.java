package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.config.security.annotations.SystemAdminOnly;
import be.kdg.integration4.config.security.annotations.TechnicianOnly;
import be.kdg.integration4.controller.api.dtos.*;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.service.implementations.CustomerServiceImpl;
import be.kdg.integration4.service.interfaces.CustomerService;
import be.kdg.integration4.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserService userService;
    private final CustomerService customerService;

    public UsersController(UserService userService, CustomerService customerService) {
        this.userService = userService;
        this.customerService = customerService;
    }

//    @SystemAdminOnly
    @GetMapping
    public ResponseEntity<List<UserWithRolesDto>> filterUsers(@RequestParam(required = false) String name,
                                                              @AuthenticationPrincipal UserDetailsImpl principal) {
        List<User> users;

        if (name != null && !name.isEmpty()) {
            users = userService.getAllFilteredByName(name);
        } else {
            users = userService.getAllWithoutLoggedInUser(principal.getUserId());
        }
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users.stream()
                .map(usr ->
                        new UserWithRolesDto(usr.getId(), usr.getName(), usr.getEmail(),
                                usr.getClass().getSimpleName().toUpperCase())).toList());
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordDto passwordDto,
                                                        @PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetailsImpl principal) {
        userService.updatePassword(id, principal.getUserId(), passwordDto.password());
        return ResponseEntity.ok().build();
    }

    @SystemAdminOnly
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bikes")
    @TechnicianOnly
    public ResponseEntity<List<BikeSearchTechnicianDto>> filterBikes(@RequestParam(required = false) String search,
                                                                            @AuthenticationPrincipal UserDetailsImpl principal) {
        User technician = userService.getUserByEmail(principal.getUsername());

        List<Bike> bikes;
        if (search != null && !search.isBlank()) {
            bikes = customerService.getCustomersRegisteredBy(technician.getId()).stream()
                    .flatMap(c -> c.getBikes().stream())
                    .filter(bike -> bike.getFrameNumber().toLowerCase().contains(search.toLowerCase())
                            || bike.getBikeModel().getBrand().toLowerCase().contains(search.toLowerCase())
                            || bike.getBikeModel().getType().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        } else {
            bikes = customerService.getCustomersRegisteredBy(technician.getId()).stream()
                    .flatMap(c -> c.getBikes().stream())
                    .toList();
        }

        if (bikes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<BikeSearchTechnicianDto> result = bikes.stream()
                .map(b -> new BikeSearchTechnicianDto(
                        b.getFrameNumber(),
                        b.getBikeModel().getType(),
                        b.getBikeModel().getBrand(),
                        b.getBikeOwner().getName()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }

}

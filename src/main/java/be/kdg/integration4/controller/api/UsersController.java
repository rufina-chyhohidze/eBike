package be.kdg.integration4.controller.api;

import be.kdg.integration4.config.security.annotations.StaffOnly;
import be.kdg.integration4.config.security.annotations.SystemAdminOnly;
import be.kdg.integration4.controller.api.dtos.CustomerDto;
import be.kdg.integration4.controller.api.dtos.PasswordDto;
import be.kdg.integration4.controller.api.dtos.UserOutputDto;
import be.kdg.integration4.controller.api.dtos.UserWithRolesDto;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.profile.User;
import be.kdg.integration4.domain.profile.UserDetailsImpl;
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

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @SystemAdminOnly
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
}

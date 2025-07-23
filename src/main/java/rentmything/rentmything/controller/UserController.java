package rentmything.rentmything.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import rentmything.rentmything.Dto.RegisterDto;
import rentmything.rentmything.Mapper.RegisterMapper;
import rentmything.rentmything.model.Role;
import rentmything.rentmything.model.User;
import rentmything.rentmything.repository.UserRepository;

@RestController
@RequestMapping("/rentMyThing")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterMapper registerMapper;

    @PostMapping("/RegAsOwner")
    public ResponseEntity<?> registerAsOwner(@RequestBody RegisterDto dto) {
        return updateUserProfile(dto, Role.OWNER);
    }

    @PostMapping("/RegAsCustomer")
    public ResponseEntity<?> registerAsCustomer(@RequestBody RegisterDto dto) {
        return updateUserProfile(dto, Role.CUSTOMER);
    }

    private ResponseEntity<?> updateUserProfile(RegisterDto dto, Role role) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please login via Google first.");
        }

        User user = userOpt.get();

        // 🚫 Prevent role change after initial registration
        if (user.getRole() != null && user.getRole() != role) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User is already registered as " + user.getRole() + ". Role cannot be changed to "
                            + role.name());
        }

        // 🧠 Use MapStruct to update non-null fields (excluding email/role)
        registerMapper.updateUserFromDto(dto, user);

        // ✅ Assign role if not set
        if (user.getRole() == null) {
            user.setRole(role);
        }

        userRepository.save(user);
        return ResponseEntity.ok("User profile updated and registered as " + user.getRole());
    }

    @GetMapping("/logged-out")
    public ResponseEntity<String> logoutMessage() {
        return ResponseEntity
                .ok("✅ You’ve been logged out successfully. You can now login again with a different account.");
    }

}

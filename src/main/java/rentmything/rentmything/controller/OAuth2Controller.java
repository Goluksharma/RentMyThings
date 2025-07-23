package rentmything.rentmything.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rentmything.rentmything.Dto.RegisterDto;
import rentmything.rentmything.Mapper.RegisterMapper;
import rentmything.rentmything.model.User;
import rentmything.rentmything.repository.UserRepository;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterMapper registerMapper;

    @GetMapping("/success")
    public ResponseEntity<?> handleGoogleLogin(OAuth2AuthenticationToken token) {
        // Extract user info from Google
        Map<String, Object> profile = token.getPrincipal().getAttributes();
        String email = (String) profile.get("email");
        String name = (String) profile.get("name");

        Optional<User> existingUser = userRepository.findByEmail(email);
        String status;

        if (existingUser.isPresent()) {
            // Existing user login
            status = "login";
        } else {
            // New user registration
            RegisterDto dto = new RegisterDto();
            dto.setEmail(email);
            dto.setName(name);

            User newUser = registerMapper.toUser(dto);
            userRepository.save(newUser);
            status = "registered";
        }

        // ✨ Return JSON response with email + name + status
        Map<String, Object> response = Map.of(
                "status", status,
                "email", email,
                "name", name);

        return ResponseEntity.ok(response);
    }

}

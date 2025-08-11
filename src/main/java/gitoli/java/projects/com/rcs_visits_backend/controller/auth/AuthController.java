package gitoli.java.projects.com.rcs_visits_backend.controller.auth;

import gitoli.java.projects.com.rcs_visits_backend.dto.request.AuthRequest;
import gitoli.java.projects.com.rcs_visits_backend.dto.request.RegisterRequest;
import gitoli.java.projects.com.rcs_visits_backend.dto.response.AuthResponse;
import gitoli.java.projects.com.rcs_visits_backend.model.User;
import gitoli.java.projects.com.rcs_visits_backend.repository.UserRepository;
import gitoli.java.projects.com.rcs_visits_backend.service.RateLimitingService;
import gitoli.java.projects.com.rcs_visits_backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RateLimitingService rateLimitingService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content)
    })
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Validate password
        if (!isPasswordValid(registerRequest.getPassword())) {
            return ResponseEntity.badRequest().body(
                    "Password must be 8+ chars with uppercase, lowercase, number, and special char"
            );
        }
        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(User.Role.valueOf(registerRequest.getRole().toUpperCase()));

        User savedUser = userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getEmail());

        // Return auth response
        AuthResponse response = new AuthResponse(
                token,
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
//        );
//        User user = userRepository.findByEmail(authRequest.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        String token = jwtUtil.generateToken(authRequest.getEmail());
//        String frontendRole = user.getRole().name().toLowerCase();
//        if (frontendRole.equals("legal")) {
//            frontendRole = "lawyer";
//        }
//        return ResponseEntity.ok(new AuthResponse(token, frontendRole));
//    }


    @Operation(summary = "Login with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials",
                    content = @Content),
            @ApiResponse(responseCode = "429", description = "Too many login attempts",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String key = "login_attempts" + ip;

//        if(!rateLimitingService.isAllowed(key, 5, 15)) { // 5 attempts per 15 mins
//            return ResponseEntity.status(429).body("Too many login attempts");
//        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(authRequest.getEmail());

        // Set HttpOnly cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); //7 days expiry
        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );
        return ResponseEntity.ok(authResponse);
    }

    private boolean isPasswordValid(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }

    @GetMapping("/verify")
    public ResponseEntity<User> verifyToken(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(jwt);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }

}
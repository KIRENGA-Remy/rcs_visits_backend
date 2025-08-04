package gitoli.java.projects.com.rcs_visits_backend.controller.auth;

import gitoli.java.projects.com.rcs_visits_backend.dto.request.AuthRequest;
import gitoli.java.projects.com.rcs_visits_backend.dto.response.AuthResponse;
import gitoli.java.projects.com.rcs_visits_backend.model.User;
import gitoli.java.projects.com.rcs_visits_backend.repository.UserRepository;
import gitoli.java.projects.com.rcs_visits_backend.service.RateLimitingService;
import gitoli.java.projects.com.rcs_visits_backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        if (!isPasswordValid(authRequest.getPassword())) {
            return ResponseEntity.badRequest().body(
                    "Password must be 8+ chars with uppercase, lowercase, number, and special char"
            );
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String key = "login_attempts" + ip;

        if(!rateLimitingService.isAllowed(key, 5, 15)) { // 5 attempts per 15 mins
            return ResponseEntity.status(429).body("Too many login attempts");
        }

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
        return ResponseEntity.ok(new AuthResponse(user.getRole().name(), token));
    }

    private boolean isPasswordValid(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }
}
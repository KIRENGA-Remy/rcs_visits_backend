//package gitoli.java.projects.com.rcs_visits_backend.service.impl;
//
//import gitoli.java.projects.com.rcs_visits_backend.dto.request.LoginRequest;
//import gitoli.java.projects.com.rcs_visits_backend.dto.response.AuthResponse;
//import gitoli.java.projects.com.rcs_visits_backend.model.User;
//import gitoli.java.projects.com.rcs_visits_backend.repository.UserRepository;
//import gitoli.java.projects.com.rcs_visits_backend.security.JwtTokenProvider;
//import gitoli.java.projects.com.rcs_visits_backend.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.apache.coyote.BadRequestException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider tokenProvider;
//
//    @Override
//    public AuthResponse login(LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = tokenProvider.generateToken(authentication);
//        User user = userRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new BadRequestException("User not found"));
//
//        return AuthResponse.builder()
//                .token(token)
//                .role(user.getRole().name())
//                .fullName(user.getFirstName() + " " + user.getLastName())
//                .build();
//    }
//}

package gitoli.java.projects.com.rcs_visits_backend.service;

import gitoli.java.projects.com.rcs_visits_backend.dto.request.LoginRequest;
import gitoli.java.projects.com.rcs_visits_backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
}

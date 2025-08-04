package gitoli.java.projects.com.rcs_visits_backend.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
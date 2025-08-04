package gitoli.java.projects.com.rcs_visits_backend.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
    public String getToken() {return token;}
}
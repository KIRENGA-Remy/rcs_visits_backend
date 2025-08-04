package gitoli.java.projects.com.rcs_visits_backend.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}

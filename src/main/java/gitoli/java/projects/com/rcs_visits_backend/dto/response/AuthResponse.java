package gitoli.java.projects.com.rcs_visits_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication response")
public class AuthResponse {
    @Schema(description = "JWT token for authentication")
    private String token;
    @Schema(description = "User's first name")
    private String firstname;
    @Schema(description = "User's last name")
    private String lastname;
    @Schema(description = "User's email address")
    private String email;
    @Schema(description = "User role (visitor, staff, admin, legal)")
    private String role;

    public AuthResponse(String token, String firstname, String lastname, String email, String role) {
        this.token = token;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }
}
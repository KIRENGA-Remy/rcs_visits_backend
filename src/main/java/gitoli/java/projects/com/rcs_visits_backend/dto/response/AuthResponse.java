package gitoli.java.projects.com.rcs_visits_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication response")
public class AuthResponse {
    @Schema(description = "JWT token for Authentication")
    @JsonProperty("token")
    private String token;

    @Schema(description = "User's first name")
    @JsonProperty("firstname")
    private String firstname;

    @Schema(description = "User's last name")
    @JsonProperty("lastname")
    private String lastname;

    @Schema(description = "User's email address")
    @JsonProperty("email")
    private String email;

    @Schema(description = "User role (visitor, staff, admin, legal)")
    @JsonProperty("role")
    private String role;

    public AuthResponse() {
    }

    public AuthResponse(String token, String firstname, String lastname, String email, String role) {
        this.token = token;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
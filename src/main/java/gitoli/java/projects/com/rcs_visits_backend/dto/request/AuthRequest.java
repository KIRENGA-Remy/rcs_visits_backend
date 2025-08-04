package gitoli.java.projects.com.rcs_visits_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "Authentication request")
public class AuthRequest {
    @Schema(description = "User's email address", example = "user@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Password (must contain 8+ chars with uppercase, lowercase, number, and special char)",
            example = "Secure@123")
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

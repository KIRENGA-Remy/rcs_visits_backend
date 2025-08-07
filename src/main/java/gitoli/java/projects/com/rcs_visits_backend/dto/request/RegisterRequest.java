package gitoli.java.projects.com.rcs_visits_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

@Schema(description = "Registration request")
public class RegisterRequest {

    @Schema(description = "User's first name", example = "John")
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @Schema(description = "Password (8+ chars with uppercase, lowercase, number, special char)",
            example = "Secure@123")
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    @Column(name = "password")
    private String password;

    @Schema(description = "User role", example = "VISITOR")
    @NotBlank
    @Column(name = "role")
    private String role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
package gitoli.java.projects.com.rcs_visits_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prisoners")
@Data
public class Prisoner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identification_number", unique = true, nullable = false)
    private String identificationNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "cell_location")
    private String cellLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PrisonerStatus status = PrisonerStatus.ACTIVE;

    public enum PrisonerStatus {
        ACTIVE, RELEASED, TRANSFERRED
    }
}
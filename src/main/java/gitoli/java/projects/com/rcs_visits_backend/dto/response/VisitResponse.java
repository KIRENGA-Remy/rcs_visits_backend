package gitoli.java.projects.com.rcs_visits_backend.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VisitResponse {
    private Long id;
    private Long visitorId;
    private Long prisonerId;
    private LocalDate requestedDate;
    private LocalTime requestedTime;
    private Integer durationMinutes;
    private String relationship;
    private String reason;
    private String status;
}

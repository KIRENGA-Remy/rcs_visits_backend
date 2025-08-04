package gitoli.java.projects.com.rcs_visits_backend.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VisitRequest {
    private Long visitorId;
    private Long prisonerId;
    private LocalDate requestedDate;
    private LocalTime requestedTime;
    private Integer durationMinutes;
    private String relationship;
    private String reason;
}

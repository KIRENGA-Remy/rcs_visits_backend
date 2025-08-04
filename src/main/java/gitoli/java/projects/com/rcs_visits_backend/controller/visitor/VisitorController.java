//package gitoli.java.projects.com.rcs_visits_backend.controller.visitor;
//
//import com.rcs.prisonsystem.dto.request.VisitRequest;
//import com.rcs.prisonsystem.dto.response.VisitResponse;
//import com.rcs.prisonsystem.service.VisitorService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/visitor")
//public class VisitorController {
//
//    private final VisitorService visitorService;
//
//    public VisitorController(VisitorService visitorService) {
//        this.visitorService = visitorService;
//    }
//
//    @PostMapping("/visits")
//    public ResponseEntity<VisitResponse> createVisitRequest(@RequestBody VisitRequest request) {
//        return ResponseEntity.ok(visitorService.createVisitRequest(request));
//    }
//
//    @GetMapping("/visits/{visitorId}")
//    public ResponseEntity<List<VisitResponse>> getVisitorVisits(@PathVariable Long visitorId) {
//        return ResponseEntity.ok(visitorService.getVisitorVisits(visitorId));
//    }
//}
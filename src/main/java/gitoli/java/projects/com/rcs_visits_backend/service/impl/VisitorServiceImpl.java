//package gitoli.java.projects.com.rcs_visits_backend.service.impl;
//
//import com.rcs.prisonsystem.dto.request.VisitRequest;
//import com.rcs.prisonsystem.dto.response.VisitResponse;
//import com.rcs.prisonsystem.model.Prisoner;
//import com.rcs.prisonsystem.model.User;
//import com.rcs.prisonsystem.model.Visit;
//import com.rcs.prisonsystem.repository.PrisonerRepository;
//import com.rcs.prisonsystem.repository.UserRepository;
//import com.rcs.prisonsystem.repository.VisitRepository;
//import com.rcs.prisonsystem.service.VisitorService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class VisitorServiceImpl implements VisitorService {
//
//    private final VisitRepository visitRepository;
//    private final UserRepository userRepository;
//    private final PrisonerRepository prisonerRepository;
//
//    public VisitorServiceImpl(VisitRepository visitRepository, UserRepository userRepository,
//                              PrisonerRepository prisonerRepository) {
//        this.visitRepository = visitRepository;
//        this.userRepository = userRepository;
//        this.prisonerRepository = prisonerRepository;
//    }
//
//    @Override
//    public VisitResponse createVisitRequest(VisitRequest request) {
//        User visitor = userRepository.findById(request.getVisitorId())
//                .orElseThrow(() -> new RuntimeException("Visitor not found"));
//
//        Prisoner prisoner = prisonerRepository.findById(request.getPrisonerId())
//                .orElseThrow(() -> new RuntimeException("Prisoner not found"));
//
//        Visit visit = new Visit();
//        visit.setVisitor(visitor);
//        visit.setPrisoner(prisoner);
//        visit.setRequestedDate(request.getRequestedDate());
//        visit.setRequestedTime(request.getRequestedTime());
//        visit.setDurationMinutes(request.getDurationMinutes());
//        visit.setRelationship(Visit.Relationship.valueOf(request.getRelationship()));
//        visit.setReason(request.getReason());
//
//        Visit savedVisit = visitRepository.save(visit);
//        return mapToVisitResponse(savedVisit);
//    }
//
//    @Override
//    public List<VisitResponse> getVisitorVisits(Long visitorId) {
//        return visitRepository.findByVisitorId(visitorId)
//                .stream()
//                .map(this::mapToVisitResponse)
//                .collect(Collectors.toList());
//    }
//
//    private VisitResponse mapToVisitResponse(Visit visit) {
//        VisitResponse response = new VisitResponse();
//        response.setId(visit.getId());
//        response.setVisitorId(visit.getVisitor().getId());
//        response.setPrisonerId(visit.getPrisoner().getId());
//        response.setRequestedDate(visit.getRequestedDate());
//        response.setRequestedTime(visit.getRequestedTime());
//        response.setDurationMinutes(visit.getDurationMinutes());
//        response.setRelationship(visit.getRelationship().name());
//        response.setReason(visit.getReason());
//        response.setStatus(visit.getStatus().name());
//        return response;
//    }
//}

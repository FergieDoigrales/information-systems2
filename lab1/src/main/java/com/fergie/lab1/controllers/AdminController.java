package com.fergie.lab1.controllers;

import com.fergie.lab1.models.MovieAudit;
import com.fergie.lab1.models.RoleRequest;
import com.fergie.lab1.models.enums.RequestStatus;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.MovieAuditService;
import com.fergie.lab1.services.RequestsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private final RequestsService requestsService;
    private final MovieAuditService movieAuditService;

    public AdminController(RequestsService requestsService, MovieAuditService movieAuditService) {
        this.requestsService = requestsService;
        this.movieAuditService = movieAuditService;
    }

    @GetMapping("/audit")
    public String getAdminStuff(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "20") int size,
                            @RequestParam(defaultValue = "username") String sort
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Pageable pageableAudit = PageRequest.of(page, size);
        Page<RoleRequest> requestsPage = requestsService.findAll(pageable);
        Page<MovieAudit> auditPage = movieAuditService.findAll(pageableAudit);
        model.addAttribute("requestsPage", requestsPage);
        model.addAttribute("auditPage", auditPage);
        model.addAttribute("currentUserId", userDetails.getId());
        model.addAttribute("userRole", userDetails.getRole().name());
        return "admin";
    }


    @PostMapping("/approve/{id}")
    public ResponseEntity<Map<String, Object>> approveRequest(@PathVariable Long id) {
        try {
            requestsService.considerRequest(id, RequestStatus.ACCEPTED);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Map<String, Object>> rejectRequest(@PathVariable Long id) {
        try {
            requestsService.considerRequest(id, RequestStatus.REJECTED);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }


}

package com.fergie.lab1.controllers;

import com.fergie.lab1.models.RoleRequest;
import com.fergie.lab1.models.enums.RequestStatus;
import com.fergie.lab1.security.CustomUserDetails;
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

    public AdminController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @GetMapping("")
    public String getAdminStuff(Model model,
                            @RequestParam(defaultValue = "0") int page,       // Номер страницы
                            @RequestParam(defaultValue = "9") int size,     // Размер страницы (по умолчанию 10)
                            @RequestParam(defaultValue = "username") String sort
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<RoleRequest> requestsPage = requestsService.findAll(pageable);
        model.addAttribute("requestsPage", requestsPage);
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

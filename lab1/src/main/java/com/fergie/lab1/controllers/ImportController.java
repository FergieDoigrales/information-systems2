package com.fergie.lab1.controllers;

import com.fergie.lab1.models.ImportAudit;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;
    private final ImportAuditService importAuditService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ImportController(ImportService importService, ImportAuditService importAuditService, SimpMessagingTemplate messagingTemplate) {
        this.importService = importService;
        this.importAuditService = importAuditService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("")
    public String showImportPage(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "15") int size,
                                 @RequestParam(defaultValue = "id") String sort) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        model.addAttribute("userRole", userDetails.getRole().name());
        Pageable pageable = PageRequest.of(page, size);
        Page<ImportAudit> audit;
        if (userDetails.getRole().name().equals("ADMIN")) {
            audit = importAuditService.findAll(pageable);
        } else {
            audit = importAuditService.findAllByAuthorId(pageable, userDetails.getId());
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("AuditPage", audit);
        return "import";
    }

    @PostMapping("/importMovies")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "15") int size) throws NoSuchAlgorithmException, IOException {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String fileHash = calculateHash(file);
        try {
            importService.importFile(file, userId, fileHash);
            messagingTemplate.convertAndSend("/topic/import-status", getAuditPage(page, size, userDetails));
            return ResponseEntity.ok(new HashMap<String, String>() {{put("message", "Imported successfully");}});
        } catch (IllegalArgumentException e) {
            messagingTemplate.convertAndSend("/topic/import-status", getAuditPage(page, size, userDetails));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, String>() {{
                put("error", "Error: " + e.getMessage());
            }});
        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/import-status", getAuditPage(page, size, userDetails));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, String>() {{
                put("error", "Errors: " + e.getMessage());
            }});
        }
    }

    private  Page<ImportAudit> getAuditPage(int page, int size, CustomUserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImportAudit> audit;
        if (userDetails.getRole().name().equals("ADMIN")) {
            audit = importAuditService.findAll(pageable);
        } else {
            audit = importAuditService.findAllByAuthorId(pageable, userDetails.getId());
        }
        return audit;
    }


    private String calculateHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(file.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

}

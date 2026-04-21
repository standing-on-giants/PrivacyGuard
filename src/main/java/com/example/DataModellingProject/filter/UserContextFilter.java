package com.example.DataModellingProject.filter;

import com.example.DataModellingProject.context.UserContext;
import com.example.DataModellingProject.repository.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {

    private final UserContext userContext;
    private final DoctorAccountRepository doctorRepo;
    private final NurseAccountRepository nurseRepo;
    private final PatientAccountRepository patientRepo;
    private final HelperAccountRepository helperRepo;
    private final AdminAccountRepository adminRepo;

    public UserContextFilter(UserContext userContext, DoctorAccountRepository doctorRepo,
                             NurseAccountRepository nurseRepo, PatientAccountRepository patientRepo,
                             HelperAccountRepository helperRepo, AdminAccountRepository adminRepo) {
        this.userContext = userContext;
        this.doctorRepo = doctorRepo;
        this.nurseRepo = nurseRepo;
        this.patientRepo = patientRepo;
        this.helperRepo = helperRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        userContext.setRequestTimestamp(java.time.Instant.now());

        userContext.addAttribute("currentDate", java.time.LocalDate.now());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // If authenticated, build the context
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {

            String email = auth.getName();
            String role = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("")
                    .replace("ROLE_", "")
                    .toLowerCase();

            userContext.setUserEmail(email);
            userContext.setRole(role);

            // Fetch the DB ID based on the role
            switch (role) {
                case "doctor":
                    doctorRepo.findByEmail(email).ifPresent(d -> userContext.setDatabaseId(String.valueOf(d.getDoctorId())));
                    break;
                case "nurse":
                    nurseRepo.findByEmail(email).ifPresent(n -> userContext.setDatabaseId(String.valueOf(n.getNurseId())));
                    break;
                case "patient":
                    patientRepo.findByEmail(email).ifPresent(p -> userContext.setDatabaseId(String.valueOf(p.getPatientId())));
                    break;
                case "helper":
                    helperRepo.findByEmail(email).ifPresent(h -> userContext.setDatabaseId(String.valueOf(h.getHelperId())));
                    break;
                case "admin":
                    adminRepo.findByEmail(email).ifPresent(a -> userContext.setDatabaseId(String.valueOf(a.getAdminId())));
                    break;
            }
        }

        // Continue the request chain
        filterChain.doFilter(request, response);
    }
}
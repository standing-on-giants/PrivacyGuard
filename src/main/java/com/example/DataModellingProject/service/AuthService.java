package com.example.DataModellingProject.service;

import com.example.DataModellingProject.dto.LoginRequest;
import com.example.DataModellingProject.dto.LoginResponse;
import com.example.DataModellingProject.model.*;
import com.example.DataModellingProject.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final DoctorAccountRepository doctorAccountRepo;
    private final NurseAccountRepository nurseAccountRepo;
    private final HelperAccountRepository helperAccountRepo;
    private final PatientAccountRepository patientAccountRepo;
    private final AdminAccountRepository adminAccountRepo;
    private final JwtService jwtService;

    public AuthService(DoctorAccountRepository doctorAccountRepo,
                       NurseAccountRepository nurseAccountRepo,
                       HelperAccountRepository helperAccountRepo,
                       PatientAccountRepository patientAccountRepo,
                       AdminAccountRepository adminAccountRepo, JwtService jwtService) {
        this.doctorAccountRepo = doctorAccountRepo;
        this.nurseAccountRepo = nurseAccountRepo;
        this.helperAccountRepo = helperAccountRepo;
        this.patientAccountRepo = patientAccountRepo;
        this.adminAccountRepo = adminAccountRepo;
        this.jwtService = jwtService;
    }

    public LoginResponse authenticate(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        String role = request.getRole().toUpperCase();

        switch (role) {
            case "DOCTOR":
                Optional<DoctorAccount> docAccount = doctorAccountRepo.findByEmail(request.getEmail());
                if (docAccount.isPresent() && docAccount.get().getPassword().equals(request.getPassword())) {
                    response.setSuccess(true);
                    response.setMessage("Doctor login successful");
                    response.setReferenceId(docAccount.get().getDoctorId());

                    String token = jwtService.generateToken(request.getEmail(), "DOCTOR", docAccount.get().getDoctorId());
                    response.setToken(token);

                    return response;
                }
                break;

            case "NURSE":
                Optional<NurseAccount> nurseAccount = nurseAccountRepo.findByEmail(request.getEmail());
                if (nurseAccount.isPresent() && nurseAccount.get().getPassword().equals(request.getPassword())) {
                    response.setSuccess(true);
                    response.setMessage("Nurse login successful");
                    response.setReferenceId(nurseAccount.get().getNurseId());

                    String token = jwtService.generateToken(request.getEmail(), "NURSE", nurseAccount.get().getNurseId());
                    response.setToken(token);

                    return response;
                }
                break;

            case "HELPER":
                Optional<HelperAccount> helperAccount = helperAccountRepo.findByEmail(request.getEmail());
                if (helperAccount.isPresent() && helperAccount.get().getPassword().equals(request.getPassword())) {
                    response.setSuccess(true);
                    response.setMessage("Helper login successful");
                    response.setReferenceId(helperAccount.get().getHelperId());

                    String token = jwtService.generateToken(request.getEmail(), "HELPER", helperAccount.get().getHelperId());
                    response.setToken(token);

                    return response;
                }
                break;

            case "PATIENT":
                Optional<PatientAccount> patientAccount = patientAccountRepo.findByEmail(request.getEmail());
                if (patientAccount.isPresent() && patientAccount.get().getPassword().equals(request.getPassword())) {
                    response.setSuccess(true);
                    response.setMessage("Patient login successful");
                    response.setReferenceId(patientAccount.get().getPatientId());

                    String token = jwtService.generateToken(request.getEmail(), "PATIENT", patientAccount.get().getPatientId());
                    response.setToken(token);

                    return response;
                }
                break;

            case "ADMIN":
                Optional<AdminAccount> adminAccount = adminAccountRepo.findByEmail(request.getEmail());
                if (adminAccount.isPresent() && adminAccount.get().getPassword().equals(request.getPassword())) {
                    response.setSuccess(true);
                    response.setMessage("Admin login successful");
                    response.setReferenceId(adminAccount.get().getAdminId());

                    String token = jwtService.generateToken(request.getEmail(), "ADMIN", adminAccount.get().getAdminId());
                    response.setToken(token);

                    return response;
                }
                break;
        }

        response.setSuccess(false);
        response.setMessage("Invalid email, password, or role.");
        return response;
    }
}
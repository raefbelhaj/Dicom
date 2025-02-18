package com.commerce.commerce.controllers.Authentification;

import com.commerce.commerce.Models.Role;
import com.commerce.commerce.Models.User;
import com.commerce.commerce.Service.Authentification.JwtService;
import com.commerce.commerce.Service.Authentification.UserService;
import com.commerce.commerce.dtos.Authentification.LoginUserDto;
import com.commerce.commerce.dtos.Authentification.UserSignupDTO;
import com.commerce.commerce.enumeration.ERole;
import com.commerce.commerce.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserSignupDTO userSignupDTO) {
        if (userService.existsByEmail(userSignupDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use.");
        }

        // Create User entity
        User user = new User();
        user.setFullName(userSignupDTO.getFullName());
        user.setEmail(userSignupDTO.getEmail());
        user.setPassword(userSignupDTO.getPassword());  // Hash this password using a password encoder

        // Get the role
        Role role = roleRepository.findByName(ERole.valueOf(userSignupDTO.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found."));
        user.setRole(role);

        // Assign specific fields based on role  :  role Patient
        if (role.getName() == ERole.ROLE_PATIENT) {
            user.setDateOfBirth(userSignupDTO.getPatientDTO().getDateOfBirth());
            user.setGender(userSignupDTO.getPatientDTO().getGender());
            user.setAddress(userSignupDTO.getPatientDTO().getAddress());

        // role doctoor

        } else if (role.getName() == ERole.ROLE_DOCTOR) {
            user.setSpecialty(userSignupDTO.getDoctorDTO().getSpecialty());
            user.setMedicalLicenseNumber(userSignupDTO.getDoctorDTO().getMedicalLicenseNumber());
            user.setHospitalOrClinic(userSignupDTO.getDoctorDTO().getHospitalOrClinic());
        }
        else if (role.getName() == ERole.ROLE_ORGANIZATION){
            user.setType(userSignupDTO.getOrganizationDTO().getType());
            user.setAdress(userSignupDTO.getOrganizationDTO().getAdress());
            user.setCity(userSignupDTO.getOrganizationDTO().getCity());
            user.setPhone(userSignupDTO.getOrganizationDTO().getPhone());

        }

        // Save the user in the database
        userService.saveUser(user);

        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = userService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        long expiresIn = jwtService.getExpirationTime();

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("expiresIn", expiresIn);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.AllUsers();
        return ResponseEntity.ok(allUsers);
    }


  /*  @GetMapping("/pagi")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<User> usersPage = userService.getUsers(page, size);  // Appeler le service avec les paramètres de pagination
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }


   */

    @GetMapping("/get/pagi")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role) {  // Le rôle est optionnel

        Page<User> usersPage = userService.getUsers(page, size, role);  // Appeler le service avec la pagination et le rôle
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }


    // get Patient By Role

    @GetMapping("/get/Patients")
    public ResponseEntity<List<User>> getPatient(){
        List<User> patients = userService.getUsersByRole("ROLE_PATIENT");
        return ResponseEntity.ok(patients);
    }


    // Get Doctor By Role
    @GetMapping("/get/Doctors")
    public ResponseEntity<List<User>> getDoctor(){
        List<User> doctors = userService.getUsersByRole("ROLE_DOCTOR");
        return ResponseEntity.ok(doctors);
    }
}

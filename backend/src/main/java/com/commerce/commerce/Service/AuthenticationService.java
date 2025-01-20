package com.commerce.commerce.Service;

import com.commerce.commerce.Models.Role;
import com.commerce.commerce.Models.User;
import com.commerce.commerce.dtos.LoginUserDto;
import com.commerce.commerce.dtos.RegisterUserDto;
import com.commerce.commerce.repositories.RoleRepository;
import com.commerce.commerce.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Injection des dépendances
    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto registerUserDto) {
        // Vérification si l'email existe déjà
        if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setFullName(registerUserDto.getFullName());

        // Récupération du rôle en fonction de l'ERole
        Role role = roleRepository.findByName(registerUserDto.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Assigner le rôle à l'utilisateur
        user.setRole(role);

        // Sauvegarde de l'utilisateur
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        // Authentifier l'utilisateur avec ses informations d'identification
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        // Si l'utilisateur n'existe pas
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

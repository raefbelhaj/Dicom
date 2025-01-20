package com.commerce.commerce.dtos;

import com.commerce.commerce.enumeration.ERole; // Importer directement la classe Role
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;
    private ERole role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ERole getRole() { // Utiliser Role
        return role;
    }

    public void setRole(ERole role) { // Utiliser Role
        this.role = role;
    }
}

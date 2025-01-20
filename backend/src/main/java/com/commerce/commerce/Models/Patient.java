package com.commerce.commerce.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private String numeroTelephone;

    public Patient (Long id, String nom, String prenom, String email, String numeroTelephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numeroTelephone = numeroTelephone;
    }

    public Patient () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getNom () {
        return nom;
    }

    public void setNom (String nom) {
        this.nom = nom;
    }

    public String getPrenom () {
        return prenom;
    }

    public void setPrenom (String prenom) {
        this.prenom = prenom;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getNumeroTelephone () {
        return numeroTelephone;
    }

    public void setNumeroTelephone (String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    // Getters et setters
}

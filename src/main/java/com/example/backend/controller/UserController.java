package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    // 1. Inscription d'un utilisateur (POST http://localhost:8080/api/users/register)
    @PostMapping("/register")
    public ResponseEntity<?> inscrireUtilisateur(@Valid @RequestBody User user) {
        try {
            User nouveauUser = userService.inscrireUtilisateur(user);
            return ResponseEntity.ok(nouveauUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Récupérer tous les utilisateurs (GET http://localhost:8080/api/users)
    @GetMapping
    public ResponseEntity<List<User>> recupererTousLesUtilisateurs() {
        return ResponseEntity.ok(userService.récupérerTousLesUtilisateurs());
    }

    // 3. Récupérer un utilisateur par son ID (GET http://localhost:8080/api/users/{id})
    @GetMapping("/{id}")
    public ResponseEntity<User> recupererParId(@PathVariable Long id) {
        return userService.trouverParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

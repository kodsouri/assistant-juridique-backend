package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // N3aytou lel moteur mta3 el-cryptage hné

    public User inscrireUtilisateur(User user) {
        // 1. Nthabtou esken el-email dkhall sbe9an walla la
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé !");
        }

        // 2. Cryptage mta3 el-password gbal el-sauvegarde
        String passwordCrypte = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordCrypte);

        // 3. Enregistrement fil base
        return userRepository.save(user);
    }

    public List<User> récupérerTousLesUtilisateurs() {
        return userRepository.findAll();
    }

    public Optional<User> trouverParId(Long id) {
        return userRepository.findById(id);
    }
}
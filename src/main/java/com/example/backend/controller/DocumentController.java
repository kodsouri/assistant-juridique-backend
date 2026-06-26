package com.example.backend.controller;

import com.example.backend.model.Document;
import com.example.backend.service.DocumentService;
import com.example.backend.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService documentService;
    private final AiService aiService;

    // 1. Ajouter un document
    @PostMapping
    public ResponseEntity<Document> ajouterDocument(@Valid @RequestBody Document document) {
        return ResponseEntity.ok(documentService.creerDocument(document));
    }

    // 2. Récupérer TOUS les documents (Pour l'Admin)
    @GetMapping
    public ResponseEntity<List<Document>> obtenirTousLesDocuments(@RequestParam(required = false) String statut) {
        if (statut != null) {
            return ResponseEntity.ok(documentService.recupererParStatut(statut.toUpperCase()));
        }
        return ResponseEntity.ok(documentService.recupererTousLesDocuments());
    }

    // 3. Recherche & Filtrage Utilisateur (APPROVED)
    @GetMapping("/search")
    public ResponseEntity<List<Document>> rechercherDocuments(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String categorie) {

        if (titre != null) {
            return ResponseEntity.ok(documentService.rechercherDocumentsPourUtilisateur(titre));
        }
        if (categorie != null) {
            return ResponseEntity.ok(documentService.filtrerParCategoriePourUtilisateur(categorie));
        }

        return ResponseEntity.ok(documentService.recupererParStatut("APPROVED"));
    }

    // 4. GENERER UN RESUME AUTOMATIQUE PAR L'IA (🛡️ Version Robuste)
    // Exemple: POST /api/documents/3/resume
    @PostMapping("/{id}/resume")
    public ResponseEntity<String> genererResumeDocument(@PathVariable Long id) {
        // Na3mlou recherche direct daxel el-liste sans restriction mta3 statut APPROVED/PENDING
        Document doc = documentService.recupererTousLesDocuments().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);

        // Thabet ken el-id mouch mawjoud
        if (doc == null) {
            return ResponseEntity.badRequest().body("Erreur : Impossible de trouver le document avec l'ID : " + id);
        }

        // Nba3thou el-contenu mte3ou lil-IA
        String resume = aiService.genererResume(doc.getContenu());

        return ResponseEntity.ok(resume);
    }

    // 5. Validation Admin : Changer le statut
    @PutMapping("/{id}/statut")
    public ResponseEntity<Document> validerDocument(
            @PathVariable Long id,
            @RequestParam String nouveauStatut) {
        return ResponseEntity.ok(documentService.changerStatutDocument(id, nouveauStatut.toUpperCase()));
    }

    // 6. Supprimer un document
    @DeleteMapping("/{id}")
    public ResponseEntity<String> effacerDocument(@PathVariable Long id) {
        documentService.supprimerDocument(id);
        return ResponseEntity.ok("Document supprime avec succes !");
    }
}
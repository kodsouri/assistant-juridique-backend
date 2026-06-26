package com.example.backend.service;

import com.example.backend.model.Document;
import com.example.backend.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    // 1. Enregistrement d'un nouveau document (dima yبدا PENDING automatic)
    public Document creerDocument(Document document) {
        return documentRepository.save(document);
    }

    // 2. Récupérer tous les documents sans exception
    public List<Document> recupererTousLesDocuments() {
        return documentRepository.findAll();
    }

    // 3. Récupérer les documents par statut (Exemple: PENDING pour l'admin)
    public List<Document> recupererParStatut(String statut) {
        return documentRepository.findByStatut(statut);
    }

    // 4. Modifier le statut d'un document (Validation Admin)
    public Document changerStatutDocument(Long id, String nouveauStatut) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erreur: Document introuvable avec l'id: " + id));

        // Nthabtou esken el-statut jdid s7i7 walla la
        if (nouveauStatut.equals("APPROVED") || nouveauStatut.equals("REJECTED") || nouveauStatut.equals("PENDING")) {
            document.setStatut(nouveauStatut);
            return documentRepository.save(document);
        } else {
            throw new RuntimeException("Erreur: Statut invalide ! Utilisez PENDING, APPROVED ou REJECTED.");
        }
    }

    // 5. Supprimer un document
    public void supprimerDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Erreur: Impossible de supprimer, document introuvable.");
        }
        documentRepository.deleteById(id);
    }
    // 6. Recherche pour l'utilisateur (Juste les documents APPROVED)
    public List<Document> rechercherDocumentsPourUtilisateur(String titre) {
        return documentRepository.findByTitreContainingIgnoreCaseAndStatut(titre, "APPROVED");
    }

    // 7. Filtrer par catégorie pour l'utilisateur (Juste les documents APPROVED)
    public List<Document> filtrerParCategoriePourUtilisateur(String categorie) {
        return documentRepository.findByCategorieIgnoreCaseAndStatut(categorie, "APPROVED");
    }
}
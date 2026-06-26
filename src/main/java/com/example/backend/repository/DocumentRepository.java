package com.example.backend.repository;

import com.example.backend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // 1. Filtrer par statut (Zdna3ha l-gbeeli lel-Admin)
    List<Document> findByStatut(String statut);

    // 2. Recherche par mot-clé dans le titre ET qui sont APPROVED (Pour l'utilisateur)
    List<Document> findByTitreContainingIgnoreCaseAndStatut(String titre, String statut);

    // 3. Filtrer par catégorie ET qui sont APPROVED (Pour l'utilisateur)
    List<Document> findByCategorieIgnoreCaseAndStatut(String categorie, String statut);
}
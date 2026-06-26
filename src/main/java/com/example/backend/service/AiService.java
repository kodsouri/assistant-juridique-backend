package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String genererResume(String contenuDocument) {
        // Thabet ken el-contenu feragh bech ma n-ba3thouch rawakh lil-IA
        if (contenuDocument == null || contenuDocument.trim().isEmpty()) {
            return "Erreur : Le contenu du document est vide !";
        }

        String prompt = "Tu es un assistant juridique expert en droit tunisien. "
                + "Fais un résumé clair, concis et professionnel du texte suivant : " + contenuDocument;

        // Payload JSON kima y-7ebha Gemini perfectly 100% ndhifa
        String jsonPayload = "{"
                + "\"contents\": [{"
                + "  \"parts\": [{"
                + "    \"text\": " + objectMapper.valueToTree(prompt).toString()
                + "  }]"
                + "}]"
                + "}";

        try {
            String responseString = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parsing safe 3al l-ekher b-JsonNode
            JsonNode root = objectMapper.readTree(responseString);
            JsonNode textNode = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (!textNode.isMissingNode()) {
                return textNode.asText();
            }

            return "Aucune réponse générée par l'IA. Response raw: " + responseString;

        } catch (Exception e) {
            return "Erreur lors de la génération du résumé par l'IA : " + e.getMessage();
        }
    }
}
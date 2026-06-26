package com.example.backend.controller;

import com.example.backend.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") // 3al jal el-CORS bech sa7bek t-testi m3ak parfaitement
public class AiController {

    private final AiService aiService;

    // L-injection mta3 el-AiService mte3ek
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarizeDocument(@RequestBody Map<String, String> request) {
        String content = request.get("content");

        // N-khaddmo el-méthode mte3ek 'genererResume' li mrigla fil-AiService
        String resumeResult = aiService.genererResume(content);

        // Kén thamma erreur fil-génération
        if (resumeResult.startsWith("Erreur")) {
            return ResponseEntity.badRequest().body(Map.of("error", resumeResult));
        }

        // N-rajj3ou el-résumé lil-frontend daxel Map
        return ResponseEntity.ok(Map.of("summary", resumeResult));
    }
}
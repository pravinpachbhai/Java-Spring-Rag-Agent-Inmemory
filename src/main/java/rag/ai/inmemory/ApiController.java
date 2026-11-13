package rag.ai.inmemory;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@Validated
public class ApiController {
    private final RagService ragService;

    public ApiController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok", "indexedChunks", ragService.count(),
                "chatModel", ragService.chatModel(), "embeddingModel", ragService.embeddingModel());
    }

    @PostMapping("/ingest/text")
    public Map<String, Object> ingestText(@Valid @RequestBody TextRequest request) {
        return ragService.ingest(request.text(), request.source() == null ? "manual-input" : request.source());
    }

    @PostMapping(value = "/ingest/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ingestFile(@RequestParam("file") MultipartFile file) throws IOException {
        String name = Optional.ofNullable(file.getOriginalFilename()).orElse("upload.txt");
        String text;
        String lower = name.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".pdf")) {
            try (org.apache.pdfbox.pdmodel.PDDocument pdf = org.apache.pdfbox.Loader.loadPDF(file.getBytes())) {
                org.apache.pdfbox.text.PDFTextStripper stripper = new org.apache.pdfbox.text.PDFTextStripper();
                text = stripper.getText(pdf);
            }
        } else if (lower.endsWith(".txt") || lower.endsWith(".md") || lower.endsWith(".csv")) {
            text = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Supported file types: PDF, TXT, MD, CSV."));
        }
        return ResponseEntity.ok(ragService.ingest(text, name));
    }

    @PostMapping("/ask")
    public Map<String, Object> ask(@Valid @RequestBody AskRequest request) {
        return ragService.ask(request.question(), request.topK() == null ? 4 : request.topK());
    }

    @PostMapping("/agent")
    public Map<String, Object> agent(@Valid @RequestBody AgentRequest request) {
        return ragService.agent(request.task());
    }
}

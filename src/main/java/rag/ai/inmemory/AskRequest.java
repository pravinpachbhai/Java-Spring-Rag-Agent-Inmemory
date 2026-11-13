package rag.ai.inmemory;

import jakarta.validation.constraints.NotBlank;

public record AskRequest(@NotBlank String question, Integer topK) {}

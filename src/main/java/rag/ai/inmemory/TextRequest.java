package rag.ai.inmemory;

import jakarta.validation.constraints.NotBlank;

public record TextRequest(@NotBlank String text, String source) {}
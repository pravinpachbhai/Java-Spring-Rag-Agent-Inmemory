package rag.ai.inmemory;

import jakarta.validation.constraints.NotBlank;

public record AgentRequest(@NotBlank String task) {}
package rag.ai.inmemory;

import java.util.List;

public record Chunk(String id, String source, int number, String text, List<Double> embedding) {}
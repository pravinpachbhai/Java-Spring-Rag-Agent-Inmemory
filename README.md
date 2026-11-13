# Java Spring Boot RAG + AI Agent (Local, Free)

Spring Boot REST API backed by a local Ollama instance. This starter uses an in-memory JSON-free vector index to keep the Java example dependency-light. Documents are embedded with Ollama and retained in memory until the app restarts.

## Requirements
- JDK 21
- Maven 3.9+
- Ollama installed and running: https://ollama.com/download/windows
- Pull models:
  ```powershell
  ollama pull qwen3:4b
  ollama pull nomic-embed-text
  ```

## Run
```powershell
cd java-spring-rag-agent
mvn spring-boot:run
```
Swagger UI is not included; use curl, Postman, or the examples below. Base URL: `http://localhost:8080`.

## API examples (PowerShell)
Ingest text:
```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/ingest/text -ContentType 'application/json' -Body '{"source":"policy.txt","text":"Employees must report incidents through the approved incident reporting system."}'
```
Ask:
```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/ask -ContentType 'application/json' -Body '{"question":"How do employees report incidents?","topK":4}'
```
Agent:
```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/agent -ContentType 'application/json' -Body '{"task":"Search the policy documents for incident reporting instructions."}'
```
Health:
```powershell
Invoke-RestMethod http://localhost:8080/health
```

## Notes
- All model inference is local; no paid API key is required.
- `/agent` is a deliberately simple, bounded workflow agent that uses retrieval and generates a final answer. It does not autonomously execute arbitrary tools.
- The Java starter keeps indexed chunks in memory. Add a persistent vector store (pgvector, Chroma, Qdrant) for production.
- Add authentication, authorization, file upload scanning, limits, observability, and tests before exposing it beyond localhost.

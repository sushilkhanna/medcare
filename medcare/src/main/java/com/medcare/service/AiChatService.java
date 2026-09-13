package com.medcare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.medcare.dto.AiChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.StringJoiner;

/**
 * Talks to a free, OpenAI-compatible chat-completions API to answer patient
 * questions on the "Buy Medicine" page — e.g. "should I take this before or
 * after food?" or "what else has the same salt as this?".
 *
 * Answers come from the model's own general knowledge (NOT the app's
 * database), per the requirement. That also means answers are informational
 * only, never a substitute for a pharmacist/doctor — the system prompt below
 * enforces that, and every reply carries a disclaimer.
 *
 * Works out of the box with Groq's free tier (no credit card required,
 * https://console.groq.com/keys), but is provider-agnostic: any
 * OpenAI-compatible /chat/completions endpoint works if you change
 * ai.api.url / ai.api.model (e.g. OpenRouter's free models).
 */
@Service
public class AiChatService {

    private static final Logger log = LoggerFactory.getLogger(AiChatService.class);

    private static final String SYSTEM_PROMPT = """
        You are MedCare Assistant, a friendly pharmacy-counter helper inside an Indian online medicine store.
        You help patients understand medicines they are about to buy: how/when to take them (e.g. before or
        after food, morning/night), common precautions, and other medicines that share the same active salt
        (generic equivalents/brand alternatives), based on your own general pharmacology knowledge.

        Rules you always follow:
        - Keep answers short: 2-5 sentences, plain language, no markdown headers.
        - Never diagnose a condition or tell someone to start/stop/change a dose.
        - If asked about dosage amounts, drug interactions, or anything for a specific medical condition,
          give general public-knowledge info only and clearly recommend confirming with a pharmacist or doctor.
        - If you don't confidently know the answer, say so honestly instead of guessing.
        - End every reply with a short one-line disclaimer such as: "This is general info, not medical advice
          — please check with a pharmacist or doctor for anything specific to you."
        """;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ai.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${ai.api.key:}")
    private String apiKey;

    // NOTE: llama-3.1-8b-instant was decommissioned by Groq on 2026-08-16.
    // openai/gpt-oss-20b is Groq's recommended free-tier replacement.
    // If you deployed before that date, update the AI_API_MODEL env var on
    // Render to "openai/gpt-oss-20b" (or another current Groq model — see
    // https://console.groq.com/docs/deprecations for the live list).
    @Value("${ai.api.model:openai/gpt-oss-20b}")
    private String model;

    public AiChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String chat(AiChatRequest req) {
        if (apiKey == null || apiKey.isBlank()) {
            return "The AI assistant isn't configured yet — ask your admin to set the AI_API_KEY "
                 + "environment variable (a free key from console.groq.com works). "
                 + "This is general info, not medical advice — please check with a pharmacist or doctor.";
        }

        String userContent = buildUserContent(req);

        ObjectNode systemMsg = objectMapper.createObjectNode();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);

        ObjectNode userMsg = objectMapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", userContent);

        ArrayNode messages = objectMapper.createArrayNode();
        messages.add(systemMsg);
        messages.add(userMsg);

        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", model);
        body.set("messages", messages);
        body.put("temperature", 0.4);
        body.put("max_tokens", 300);

        try {
            JsonNode response = webClient.post()
                    .uri(UriComponentsBuilder.fromHttpUrl(apiUrl).toUriString())
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(body.toString())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(20))
                    .block();

            if (response == null) throw new IllegalStateException("Empty response from AI provider");

            JsonNode choices = response.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                String content = choices.get(0).path("message").path("content").asText("");
                if (!content.isBlank()) return content.trim();
            }
            log.warn("AI provider returned no usable content: {}", response);
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // Surface the provider's actual status + body in the server log
            // (never to the client) — this is what tells you *why* it failed:
            // 401 = bad/rotated API key, 404 = wrong/decommissioned model
            // name, 429 = rate limited. Check Render's logs for this line.
            log.error("AI chat call failed: HTTP {} from provider — body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("AI chat call failed", e);
        }
        return "Sorry, I couldn't reach the AI assistant right now. Please try again in a moment, "
             + "or ask your pharmacist directly.";
    }

    private String buildUserContent(AiChatRequest req) {
        StringJoiner ctx = new StringJoiner(", ");
        if (notBlank(req.getMedicineName())) ctx.add("Medicine: " + req.getMedicineName());
        if (notBlank(req.getMedicineBrand())) ctx.add("Brand: " + req.getMedicineBrand());
        if (notBlank(req.getMedicineCategory())) ctx.add("Category: " + req.getMedicineCategory());

        if (ctx.length() == 0) return req.getMessage();
        return "[Context: " + ctx + "]\nPatient question: " + req.getMessage();
    }

    private boolean notBlank(String s) { return s != null && !s.isBlank(); }
}

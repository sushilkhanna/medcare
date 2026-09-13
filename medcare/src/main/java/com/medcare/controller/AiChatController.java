package com.medcare.controller;

import com.medcare.dto.AiChatRequest;
import com.medcare.dto.AiChatResponse;
import com.medcare.dto.ApiResponse;
import com.medcare.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authenticated-only (any logged-in role) so the free AI quota isn't
 * exposed to anonymous internet traffic. Requires a Bearer token, same as
 * cart/orders — enforced by SecurityConfig's default "anyRequest().authenticated()".
 */
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<AiChatResponse>> chat(@Valid @RequestBody AiChatRequest req) {
        String reply = aiChatService.chat(req);
        return ResponseEntity.ok(ApiResponse.ok("AI reply", new AiChatResponse(reply)));
    }
}

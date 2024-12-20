package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.service.OpenAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OpenAIController {

    private final OpenAIService openAIService;

    @GetMapping("/models")
    public ResponseEntity<List<String>> getModels() {
        return ResponseEntity.ok(openAIService.getModels());
    }

    @PostMapping("/generate/text")
    public ResponseEntity<String> getJsonResponseWithSettings(@Valid @RequestBody OpenAIChatRequest openAIChatRequest) {
        String response = openAIService.getTextResponse(openAIChatRequest);
        return ResponseEntity.ok(response);
    }
}

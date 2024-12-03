package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.BookDto;
import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/generate")
public class OpenAIController {

    private final OpenAIService openAIService;

    @PostMapping("/json")
    public ResponseEntity<List<BookDto>> getJsonResponse(@RequestBody OpenAIChatRequest chatRequest) {
        List<BookDto> response = openAIService.getJsonResponse(chatRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/text/history")
    public ResponseEntity<String> getTextResponseWithHistory(@RequestBody OpenAIChatRequest chatRequest) {
        String response = openAIService.getTextResponseWithHistory(chatRequest);
        return ResponseEntity.ok(response);
    }
}


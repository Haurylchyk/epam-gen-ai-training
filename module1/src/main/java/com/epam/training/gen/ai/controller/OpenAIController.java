package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.BookDto;
import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.service.SemanticKernelService;
import jakarta.validation.Valid;
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

    private final SemanticKernelService semanticKernelService;

    @PostMapping("/json")
    public ResponseEntity<List<BookDto>> getJsonResponseWithSettings(@Valid @RequestBody OpenAIChatRequest openAIChatRequest) {
        List<BookDto> response = semanticKernelService.receiveBookList(openAIChatRequest);
        return ResponseEntity.ok(response);
    }
}

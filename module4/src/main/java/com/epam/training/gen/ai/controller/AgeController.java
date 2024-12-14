package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.OpenAIChatRequest;
import com.epam.training.gen.ai.model.OpenAIChatResponse;
import com.epam.training.gen.ai.service.AIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AgeController {

    private final AIService aiService;

    @Autowired
    public AgeController(@Qualifier("ageCalculationService") AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(path = "/calculate/age")
    public @ResponseBody ResponseEntity<OpenAIChatResponse> getAge(@RequestBody @Valid OpenAIChatRequest request) {
        return aiService.getKernelFunctionalResponse(request.getQuery())
                .map(OpenAIChatResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }
}

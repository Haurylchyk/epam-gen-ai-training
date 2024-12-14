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
public class WeatherForecastController {
    private final AIService aiService;

    @Autowired
    public WeatherForecastController(@Qualifier("weatherForecastService") AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(path = "/forecast/weather")
    public @ResponseBody ResponseEntity<OpenAIChatResponse> getTimeanddateUrl(@RequestBody @Valid OpenAIChatRequest request) {
        return aiService.getKernelFunctionalResponse(request.getQuery())
                .map(OpenAIChatResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }
}

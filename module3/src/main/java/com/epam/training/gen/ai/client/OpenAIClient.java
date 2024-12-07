package com.epam.training.gen.ai.client;

import com.epam.training.gen.ai.dto.OpenAIModelList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openAiClient", url = "${client-azureopenai-endpoint}")
public interface OpenAIClient {
    @GetMapping("/openai/deployments")
    OpenAIModelList getModels(@RequestHeader("Api-Key") String apiKey);
}

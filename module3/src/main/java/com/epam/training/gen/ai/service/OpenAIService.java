package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.client.OpenAIClient;
import com.epam.training.gen.ai.dto.BookDto;
import com.epam.training.gen.ai.dto.Deployment;
import com.epam.training.gen.ai.dto.DeploymentList;
import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.exception.OpenAIException;
import com.epam.training.gen.ai.validation.DeploymentNameValidator;
import com.epam.training.gen.ai.validation.ValidationConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAIService {

    @Value("${client-azureopenai-key}")
    private String azureClientKey;

    private final ChatCompletionService.Builder chatCompletionServiceBuilder;
    private final DeploymentNameValidator deploymentNameValidator;
    private final OpenAIClient openAiClient;
    private final ObjectMapper objectMapper;

    public List<String> getModels() {
        DeploymentList response = openAiClient.getDeployments(azureClientKey);

        if (response == null) {
            throw new OpenAIException(ValidationConstants.GETTING_MODELS_FAILED);
        }

        return response.getData()
                .stream()
                .map(Deployment::getModel)
                .toList();
    }

    public List<BookDto> receiveBookList(OpenAIChatRequest request) {
        String deploymentName = Optional.ofNullable(request.getDeploymentName())
                .filter(deploymentNameValidator::isValid)
                .orElseThrow(() -> new OpenAIException(ValidationConstants.INVALID_DEPLOYMENT_NAME));

        FunctionResult<Object> response = invokePrompt(
                buildKernel(deploymentName),
                createJsonPrompt(request.getInput()),
                createPromptExecutionSettings(request)
        );
        String result = Objects.requireNonNull(response.getResult()).toString();
        return receiveBooks(result);
    }

    private Kernel buildKernel(String deploymentName) {
        ChatCompletionService chatCompletionService = chatCompletionServiceBuilder
                .withModelId(deploymentName)
                .withDeploymentName(deploymentName)
                .build();
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    private FunctionResult<Object> invokePrompt(Kernel kernel, String userInput, PromptExecutionSettings settings) {
        return kernel.invokePromptAsync(userInput)
                .withPromptExecutionSettings(settings)
                .block();
    }

    private PromptExecutionSettings createPromptExecutionSettings(OpenAIChatRequest openAIChatRequest) {
        return PromptExecutionSettings.builder()
                .withMaxTokens(Optional.ofNullable(openAIChatRequest.getMaxTokens())
                        .orElse(ValidationConstants.MAX_TOKENS_DEFAULT_VALUE))
                .withTemperature(Optional.ofNullable(openAIChatRequest.getTemperature())
                        .orElse(ValidationConstants.TEMPERATURE_DEFAULT_VALUE))
                .build();
    }

    private List<BookDto> receiveBooks(String result) {
        try {
            List<BookDto> bookList = objectMapper.readValue(result, new TypeReference<>() {
            });
            log.info("Result: {}", bookList);
            return bookList;
        } catch (Exception e) {
            log.error(ValidationConstants.PROCESS_JSON_FAILED, e);
            throw new OpenAIException(ValidationConstants.PROCESS_JSON_FAILED + e.getMessage(), e);
        }
    }

    private String createJsonPrompt(String input) {
        String template = """
                ## Instructions
                Provide a list of books that match the user's request in the following format
                [
                    {
                        "title": "title1",
                        "genre": "genre1",
                        "author": "author1",
                        "pageCount": "pageCount1"
                    },
                    {
                        "title": "title2",
                        "genre": "genre2",
                        "author": "author2",
                        "pageCount": "pageCount2"
                    }
                ]
                ## User Input
                User is looking for: "%s"
                """;
        return String.format(template, input);
    }

}

package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.client.OpenAIClient;
import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.dto.OpenAIModel;
import com.epam.training.gen.ai.dto.OpenAIModelList;
import com.epam.training.gen.ai.exception.OpenAIException;
import com.epam.training.gen.ai.validation.ModelNameValidator;
import com.epam.training.gen.ai.validation.ValidationConstants;
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
    private final ModelNameValidator modelNameValidator;
    private final OpenAIClient openAIClient;

    public List<String> getModels() {
        OpenAIModelList response = openAIClient.getModels(azureClientKey);

        if (response == null) {
            throw new OpenAIException(ValidationConstants.GETTING_MODELS_FAILED);
        }

        return response.getData()
                .stream()
                .map(OpenAIModel::getModel)
                .toList();
    }

    public String getTextResponse(OpenAIChatRequest request) {
        String deploymentName = Optional.ofNullable(request.getDeploymentName())
                .filter(modelNameValidator::isValid)
                .orElseThrow(() -> new OpenAIException(ValidationConstants.INVALID_DEPLOYMENT_NAME));

        FunctionResult<Object> response = invokePrompt(
                buildKernel(deploymentName),
                request.getInput(),
                createPromptExecutionSettings(request)
        );
        return Objects.requireNonNull(response.getResult()).toString();

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
}

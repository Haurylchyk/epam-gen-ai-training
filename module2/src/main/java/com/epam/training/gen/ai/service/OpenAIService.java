package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.dto.BookDto;
import com.epam.training.gen.ai.dto.OpenAIChatRequest;
import com.epam.training.gen.ai.exception.OpenAIException;
import com.epam.training.gen.ai.validation.ValidationConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAIService {

    private static final String INPUT_LOG_PREFIX = "User input: {}";
    private static final String RESULT_LOG_PREFIX = "Received result: {}";
    private static final String HISTORY_LOG_PREFIX = "Messages in chat history: {}";
    private static final String JSON_RESULT_LOG_PREFIX = "JSON mapping result: {}";

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final ChatHistory chatHistory;
    private final ObjectMapper objectMapper;

    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    public List<BookDto> getJsonResponse(OpenAIChatRequest chatRequest) {
        log.info(INPUT_LOG_PREFIX, chatRequest.getInput());
        String userInput = chatRequest.getInput();
        PromptExecutionSettings settings = createPromptExecutionSettings(chatRequest);

        FunctionResult<Object> response = kernel.invokePromptAsync(createJsonPrompt(userInput))
                .withPromptExecutionSettings(settings)
                .block();

        String result = response.getResult().toString();
        log.info(RESULT_LOG_PREFIX, result);

        return receiveBooks(result);
    }

    public List<BookDto> getJsonResponseWithHistory(OpenAIChatRequest chatRequest) {
        log.info(INPUT_LOG_PREFIX, chatRequest.getInput());
        String userInput = chatRequest.getInput();
        PromptExecutionSettings settings = createPromptExecutionSettings(chatRequest);

        chatHistory.addUserMessage(createJsonPrompt(userInput));

        List<ChatMessageContent<?>> response = chatCompletionService
                .getChatMessageContentsAsync(
                        chatHistory,
                        kernel,
                        InvocationContext.builder()
                                .withPromptExecutionSettings(settings)
                                .build())
                .block();

        String result = response.get(response.size() - 1).getContent();
        chatHistory.addAssistantMessage(result);
        log.info(RESULT_LOG_PREFIX, result);
        log.info(HISTORY_LOG_PREFIX, chatHistory.getMessages().size());
        return receiveBooks(result);
    }

    private PromptExecutionSettings createPromptExecutionSettings(OpenAIChatRequest openAIChatRequest) {
        return PromptExecutionSettings.builder()
                .withMaxTokens(Optional.ofNullable(openAIChatRequest.getMaxTokens())
                        .orElse(ValidationConstants.MAX_TOKENS_DEFAULT_VALUE))
                .withTemperature(Optional.ofNullable(openAIChatRequest.getTemperature())
                        .orElse(ValidationConstants.TEMPERATURE_DEFAULT_VALUE))
                .build();
    }

    private List<BookDto> receiveBooks(String jsonInput) {
        try {
            List<BookDto> bookList = objectMapper.readValue(jsonInput, new TypeReference<>() {
            });
            log.info(JSON_RESULT_LOG_PREFIX, bookList);
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

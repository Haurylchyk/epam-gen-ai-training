package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractKernelService implements AIService {

    private static final String PROMPT = """
            <message role="system">{{system}}</message>
            <message role="user">{{request}}</message>
            """;
    protected static final String TEMPLATE_FORMAT = "handlebars";
    protected static final String SYSTEM_ARG_KEY = "system";
    protected static final String USER_ARG_KEY = "request";

    private final ChatCompletionService chatCompletionService;
    private final PromptExecutionSettings promptExecutionSettings;

    protected abstract KernelPlugin getPlugin();

    protected abstract String getSystemPrompt();

    protected abstract String getFunctionName();

    protected abstract String getTemplate();

    @Override
    public Optional<String> getKernelFunctionalResponse(String message) {

        var plugin = getPlugin();

        var kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(plugin)
                .build();

        var getIntent = KernelFunction.<String>createFromPrompt(getPrompt())
                .withTemplateFormat(TEMPLATE_FORMAT)
                .build();

        var arguments = KernelFunctionArguments.builder()
                .withVariable(SYSTEM_ARG_KEY, getSystemPrompt())
                .withVariable(USER_ARG_KEY, message)
                .build();

        FunctionResult<String> block = kernel
                .invokeAsync(getIntent)
                .withPromptExecutionSettings(promptExecutionSettings)
                .withArguments(arguments)
                .withToolCallBehavior(ToolCallBehavior.allowOnlyKernelFunctions(true, plugin.get(getFunctionName())))
                .block();

        return Optional.ofNullable(block)
                .map(FunctionResult::getResult);
    }

    protected String getPrompt() {
        return PROMPT;
    }
}

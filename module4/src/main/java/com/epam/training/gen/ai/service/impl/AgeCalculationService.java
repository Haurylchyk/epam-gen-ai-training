package com.epam.training.gen.ai.service.impl;

import com.epam.training.gen.ai.service.AbstractKernelService;
import com.epam.training.gen.ai.service.plugin.AgeCalculatorPlugin;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.stereotype.Service;

import static com.epam.training.gen.ai.service.plugin.AgeCalculatorPlugin.AGE_TEMPLATE;
import static com.epam.training.gen.ai.service.plugin.AgeCalculatorPlugin.AGE_TEMPLATE_PLUGIN;

@Service
public class AgeCalculationService extends AbstractKernelService {

    private static final String SYSTEM_PROMPT = """
                Your task is to calculate the age of a person based on the entered date.The input will provide the birth year, month, and day.
                Use the 'calculateAge' function to return the age in years, months, and days.Use the provided template for the response: %s.
                """;

    public AgeCalculationService(ChatCompletionService chatCompletionService, PromptExecutionSettings promptExecutionSettings) {
        super(chatCompletionService, promptExecutionSettings);
    }

    @Override
    protected KernelPlugin getPlugin() {
        return KernelPluginFactory.createFromObject(new AgeCalculatorPlugin(), getFunctionName());
    }

    @Override
    protected String getSystemPrompt() {
        return String.format(SYSTEM_PROMPT, getTemplate());
    }

    @Override
    protected String getFunctionName() {
        return AGE_TEMPLATE_PLUGIN;
    }

    @Override
    protected String getTemplate() {
        return AGE_TEMPLATE;
    }
}

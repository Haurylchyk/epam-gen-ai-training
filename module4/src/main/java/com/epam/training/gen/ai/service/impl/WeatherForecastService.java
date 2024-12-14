package com.epam.training.gen.ai.service.impl;

import com.epam.training.gen.ai.service.AbstractKernelService;
import com.epam.training.gen.ai.service.plugin.WeatherForecastPlugin;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.stereotype.Service;

import static com.epam.training.gen.ai.service.plugin.WeatherForecastPlugin.WEATHER_FORECAST_URL_PLUGIN;
import static com.epam.training.gen.ai.service.plugin.WeatherForecastPlugin.WEATHER_URL_TEMPLATE;

@Service
public class WeatherForecastService extends AbstractKernelService {

    private static final String SYSTEM_PROMPT = """
                Your task is to help users create links to search for the weather forecast in Timeanddate. The input will be a search query.
                Use the 'getWeatherForecastUrl' function to generate a valid Timeanddate search URL. Use the provided template for the response: %s.
                """;

    public WeatherForecastService(ChatCompletionService chatCompletionService, PromptExecutionSettings promptExecutionSettings) {
        super(chatCompletionService, promptExecutionSettings);
    }

    @Override
    protected KernelPlugin getPlugin() {
        return KernelPluginFactory.createFromObject(new WeatherForecastPlugin(), getFunctionName());
    }

    @Override
    protected String getSystemPrompt() {
        return String.format(SYSTEM_PROMPT, getTemplate());
    }

    @Override
    protected String getFunctionName() {
        return WEATHER_FORECAST_URL_PLUGIN;
    }

    @Override
    protected String getTemplate() {
        return WEATHER_URL_TEMPLATE;
    }
}

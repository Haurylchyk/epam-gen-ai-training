package com.epam.training.gen.ai.service.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherForecastPlugin {

    public static final String WEATHER_FORECAST_URL_PLUGIN = "getWeatherForecastUrl";
    public static final String WEATHER_URL_TEMPLATE = "https://www.timeanddate.com/weather/?query=%s";

    @DefineKernelFunction(name = "getWeatherForecastUrl", description = "Return URL for weather forecast query")
    public String getWeatherForecastUrl(
            @KernelFunctionParameter(description = "Text to forecast weather for", name = "query") String query
    ) {
        var encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return WEATHER_URL_TEMPLATE.formatted(encoded);
    }
}

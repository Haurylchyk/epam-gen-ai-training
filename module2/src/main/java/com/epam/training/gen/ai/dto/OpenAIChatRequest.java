package com.epam.training.gen.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.epam.training.gen.ai.validation.ValidationConstants.MAX_TOKENS_MAX_VALUE;
import static com.epam.training.gen.ai.validation.ValidationConstants.MAX_TOKENS_MAX_VALUE_ERROR;
import static com.epam.training.gen.ai.validation.ValidationConstants.MAX_TOKENS_MIN_VALUE;
import static com.epam.training.gen.ai.validation.ValidationConstants.MAX_TOKENS_MIN_VALUE_ERROR;
import static com.epam.training.gen.ai.validation.ValidationConstants.INPUT_NOT_NULL;
import static com.epam.training.gen.ai.validation.ValidationConstants.TEMPERATURE_MAX_VALUE;
import static com.epam.training.gen.ai.validation.ValidationConstants.TEMPERATURE_MAX_VALUE_ERROR;
import static com.epam.training.gen.ai.validation.ValidationConstants.TEMPERATURE_MIN_VALUE;
import static com.epam.training.gen.ai.validation.ValidationConstants.TEMPERATURE_MIN_VALUE_ERROR;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class OpenAIChatRequest {

    /**
     * prompt is a set of instructions given to an AI system to perform a task.
     */
    @NotNull(message = INPUT_NOT_NULL)
    @JsonProperty("input")
    private String input;

    /**
     * maxTokens is a parameter that determines the maximum output length of the AI's response.
     */
    @Min(value = MAX_TOKENS_MIN_VALUE, message = MAX_TOKENS_MIN_VALUE_ERROR)
    @Max(value = MAX_TOKENS_MAX_VALUE, message = MAX_TOKENS_MAX_VALUE_ERROR)
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * temperature is a parameter that influences the "creativity" and randomness of the AI's output.
     * Its value ranges between 0 and 1. A lower temperature makes the output more deterministic and focused,
     * while a higher temperature increases diversity and variety in responses.
     */
    @Min(value = TEMPERATURE_MIN_VALUE, message = TEMPERATURE_MIN_VALUE_ERROR)
    @Max(value = TEMPERATURE_MAX_VALUE, message = TEMPERATURE_MAX_VALUE_ERROR)
    @JsonProperty("temperature")
    private Double temperature;
}

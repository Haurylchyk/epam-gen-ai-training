package com.epam.training.gen.ai.model;

import com.epam.training.gen.ai.validation.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OpenAIChatRequest {
    @NotBlank(message = ValidationConstants.QUERY_NOT_BLANK)
    private String query;
}

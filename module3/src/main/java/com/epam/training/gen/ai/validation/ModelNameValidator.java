package com.epam.training.gen.ai.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ModelNameValidator {

    private final List<String> validModelNames;

    public boolean isValid(String value) {
        return validModelNames.contains(value);
    }
}

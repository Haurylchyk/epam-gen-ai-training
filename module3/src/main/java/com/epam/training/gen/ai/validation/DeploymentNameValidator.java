package com.epam.training.gen.ai.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeploymentNameValidator {

    private final List<String> validDeploymentNames;

    public boolean isValid(String value) {
        return validDeploymentNames.contains(value);
    }
}

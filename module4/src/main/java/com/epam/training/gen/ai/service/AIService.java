package com.epam.training.gen.ai.service;

import java.util.Optional;

@FunctionalInterface
public interface AIService {
    Optional<String> getKernelFunctionalResponse(String message);
}

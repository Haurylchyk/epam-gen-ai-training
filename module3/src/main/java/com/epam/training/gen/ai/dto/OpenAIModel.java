package com.epam.training.gen.ai.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OpenAIModel {
    private String id;
    private String model;
}

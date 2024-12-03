package com.epam.training.gen.ai.validation;

public class ValidationConstants {

    public static final int MAX_TOKENS_DEFAULT_VALUE = 500;
    public static final int MAX_TOKENS_MIN_VALUE = 1;
    public static final int MAX_TOKENS_MAX_VALUE = 1000;

    public static final double TEMPERATURE_DEFAULT_VALUE = 0.5;
    public static final int TEMPERATURE_MIN_VALUE = 0;
    public static final int TEMPERATURE_MAX_VALUE = 1;

    public static final String INPUT_NOT_NULL = "Input cannot be null";
    public static final String MAX_TOKENS_MIN_VALUE_ERROR = "maxTokens must be at least 500";
    public static final String MAX_TOKENS_MAX_VALUE_ERROR = "maxTokens must be less than or equal to 5000";
    public static final String TEMPERATURE_MIN_VALUE_ERROR = "temperature must be at least 0.0";
    public static final String TEMPERATURE_MAX_VALUE_ERROR = "temperature must be less than or equal to 1.0";
    public static final String PROCESS_JSON_FAILED = "Error during JSON mapping: ";
    public static final String PROCESS_REQUEST_FAILED = "Failed to process the request: ";
}

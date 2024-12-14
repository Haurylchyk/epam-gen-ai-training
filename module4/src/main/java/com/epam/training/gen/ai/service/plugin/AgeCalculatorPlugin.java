package com.epam.training.gen.ai.service.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculatorPlugin {
    public static final String AGE_TEMPLATE_PLUGIN = "calculateAge";
    public static final String AGE_TEMPLATE = "Your age is: %d years, %d months, and %d days";

    @DefineKernelFunction(
            name = "calculateAge",
            description = "Calculate the age based on the birth year, month, and day")
    public String calculateAge(
            @KernelFunctionParameter(name = "year", description = "Year of birth") String birthYear,
            @KernelFunctionParameter(name = "month", description = "Month of birth") String birthMonth,
            @KernelFunctionParameter(name = "day", description = "Day of birth") String birthDay) {

        var birthDate = LocalDate.of(
                Integer.parseInt(birthYear),
                Integer.parseInt(birthMonth),
                Integer.parseInt(birthDay));

        var period = Period.between(birthDate, LocalDate.now());
        return String.format(AGE_TEMPLATE, period.getYears(), period.getMonths(), period.getDays());
    }
}

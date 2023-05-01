package com.brocollic.newsapp.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EnumValidator implements ConstraintValidator<EnumValue, CharSequence> {

    private List<String> allowedValues;

    @Override
    public void initialize(EnumValue annotation) {
        allowedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return allowedValues.contains(value.toString());
    }
}

package com.universalconverter.core;

import java.util.LinkedHashMap;
import java.util.Map;

/** Immutable description of one file conversion. */
public record ConversionConfig(String source, String target, String type, Map<String, Object> options) {
    public ConversionConfig {
        if (isBlank(type)) {
            throw new IllegalArgumentException("type is required");
        }
        options = options == null ? Map.of() : Map.copyOf(new LinkedHashMap<>(options));
    }

    private static boolean isBlank(String value) { return value == null || value.isBlank(); }

    public String optionAsString(String name, String defaultValue) {
        Object value = options.get(name);
        return value == null ? defaultValue : String.valueOf(value);
    }

    public boolean optionAsBoolean(String name, boolean defaultValue) {
        Object value = options.get(name);
        return value == null ? defaultValue : Boolean.parseBoolean(String.valueOf(value));
    }
}

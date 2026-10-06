package com.universalconverter.core;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Thread-safe registry of conversion adapters. */
public final class ConverterRegistry {
    private final Map<String, Converter> converters = new ConcurrentHashMap<>();
    public ConverterRegistry register(Converter converter) {
        converters.put(normalize(converter.type()), converter);
        return this;
    }
    public Optional<Converter> find(String type) { return Optional.ofNullable(converters.get(normalize(type))); }
    private static String normalize(String type) { return type.toLowerCase(Locale.ROOT).trim(); }
}

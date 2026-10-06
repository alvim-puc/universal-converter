package com.universalconverter.config;

import com.universalconverter.core.ConversionConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class YamlConfigLoader {
    private final Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
    public ConversionConfig load(Path path) throws IOException {
        try (InputStream input = Files.newInputStream(path)) { return load(input); }
    }
    @SuppressWarnings("unchecked")
    public ConversionConfig load(InputStream input) {
        Object loaded = yaml.load(input);
        if (!(loaded instanceof Map<?, ?> map)) throw new IllegalArgumentException("Configuration must be a YAML mapping");
        Map<String, Object> values = new LinkedHashMap<>();
        map.forEach((key, value) -> values.put(String.valueOf(key), value));
        Object rawOptions = values.get("options");
        Map<String, Object> options = rawOptions instanceof Map<?, ?> optionMap
                ? optionMap.entrySet().stream().collect(LinkedHashMap::new, (m, e) -> m.put(String.valueOf(e.getKey()), e.getValue()), Map::putAll)
                : Map.of();
        return new ConversionConfig(stringValue(values, "source"), stringValue(values, "target"), stringValue(values, "type"), options);
    }
    private static String stringValue(Map<String, Object> values, String key) {
        Object value = values.get(key); return value == null ? null : String.valueOf(value);
    }
}

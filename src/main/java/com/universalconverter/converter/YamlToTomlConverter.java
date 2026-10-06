package com.universalconverter.converter;

import com.moandjiezana.toml.TomlWriter;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class YamlToTomlConverter implements Converter {
    private final Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
    @Override public String type() { return "yaml-to-toml"; }
    @Override @SuppressWarnings("unchecked") public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException {
        Object value = yaml.load(input); if (!(value instanceof Map<?, ?>)) throw new IllegalArgumentException("TOML root must be a mapping");
        Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        new TomlWriter().write((Map<String, Object>) value, writer);
        writer.flush();
    }
}

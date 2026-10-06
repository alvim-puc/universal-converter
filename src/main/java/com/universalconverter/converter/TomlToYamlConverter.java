package com.universalconverter.converter;

import com.moandjiezana.toml.Toml;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.charset.StandardCharsets;

public final class TomlToYamlConverter implements Converter {
    private final Yaml yaml;
    public TomlToYamlConverter() { DumperOptions options = new DumperOptions(); options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); yaml = new Yaml(options); }
    @Override public String type() { return "toml-to-yaml"; }
    @Override public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException {
        Toml toml = new Toml().read(input);
        Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        yaml.dump(toml.toMap(), writer);
        writer.flush();
    }
}

package com.universalconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import java.io.*;

public final class YamlToJsonConverter implements Converter {
    private final Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions())); private final ObjectMapper json = new ObjectMapper();
    @Override public String type() { return "yaml-to-json"; }
    @Override public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException { json.writerWithDefaultPrettyPrinter().writeValue(output, yaml.load(input)); }
}

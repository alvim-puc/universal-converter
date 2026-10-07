package com.universalconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.*;

public final class JsonToYamlConverter implements Converter {
    private final Yaml yaml;
    private final ObjectMapper json = new ObjectMapper();
    public JsonToYamlConverter() {
        DumperOptions opts = new DumperOptions();
        opts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(opts);
    }
    @Override public String type() { return "json-to-yaml"; }
    @Override public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException { yaml.dump(json.readValue(input, Object.class), new OutputStreamWriter(output)); }
}

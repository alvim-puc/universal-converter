package com.universalconverter.cli;

import com.universalconverter.config.YamlConfigLoader;
import com.universalconverter.converter.*;
import com.universalconverter.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.*;

public final class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private Main() { }
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 3) { usage(); System.exit(2); }
        try {
            run(Path.of(args[0]), args.length > 1 ? Path.of(args[1]) : null, args.length > 2 ? Path.of(args[2]) : null);
        } catch (Exception e) { LOG.error("Conversion failed: {}", e.getMessage()); System.err.println("Conversion failed: " + e.getMessage()); System.exit(1); }
    }
    public static void run(Path configFile) throws IOException { run(configFile, null, null); }
    /** Command-line paths override paths in YAML; absent paths fall back to YAML values. */
    public static void run(Path configFile, Path sourceOverride, Path targetOverride) throws IOException {
        ConversionConfig config = new YamlConfigLoader().load(configFile);
        Converter converter = registry().find(config.type()).orElseThrow(() -> new IllegalArgumentException("Unsupported conversion type: " + config.type()));
        Path source = sourceOverride != null ? sourceOverride : configuredPath(config.source(), "source");
        Path target = (targetOverride != null ? targetOverride : configuredPath(config.target(), "target")).toAbsolutePath();
        Path parent = target.getParent(); if (parent != null) Files.createDirectories(parent);
        try (InputStream input = Files.newInputStream(source); OutputStream output = Files.newOutputStream(target)) { converter.convert(input, output, config); }
        LOG.info("Converted {} to {} using {}", source, target, config.type());
    }
    private static Path configuredPath(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Missing " + name + ": provide it in the CLI or config.yaml");
        return Path.of(value);
    }
    private static void usage() { System.err.println("Usage: java -jar universal-converter-1.0.0.jar <config.yaml> [input-file] [output-file]"); }
    private static ConverterRegistry registry() { return new ConverterRegistry().register(new CsvToJsonConverter()).register(new JsonToCsvConverter()).register(new XmlToJsonConverter()).register(new YamlToJsonConverter()).register(new YamlToTomlConverter()).register(new TomlToYamlConverter()); }
}

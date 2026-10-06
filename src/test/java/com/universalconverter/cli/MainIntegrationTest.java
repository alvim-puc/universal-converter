package com.universalconverter.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class MainIntegrationTest {
    @TempDir Path directory;
    @Test void convertsFilesFromYamlConfiguration() throws Exception {
        Path source = directory.resolve("input.csv"); Path target = directory.resolve("output.json"); Path config = directory.resolve("config.yaml");
        Files.writeString(source, "name,score\nAda,100\n");
        Files.writeString(config, "source: '" + source + "'\ntarget: '" + target + "'\ntype: csv-to-json\n");
        Main.run(config);
        assertEquals(100, new ObjectMapper().readTree(Files.readAllBytes(target)).get(0).get("score").asInt());
    }
    @Test void cliPathsOverrideYamlPathsAndTargetCanBeDefaulted() throws Exception {
        Path configuredSource = directory.resolve("configured.csv"); Path source = directory.resolve("actual.csv");
        Path defaultTarget = directory.resolve("default.json"); Path overriddenTarget = directory.resolve("override.json"); Path config = directory.resolve("config.yaml");
        Files.writeString(configuredSource, "name\nConfigured\n"); Files.writeString(source, "name\nCLI\n");
        Files.writeString(config, "source: '" + configuredSource + "'\ntarget: '" + defaultTarget + "'\ntype: csv-to-json\n");
        Main.run(config, source, null);
        assertEquals("CLI", new ObjectMapper().readTree(Files.readAllBytes(defaultTarget)).get(0).get("name").asText());
        Main.run(config, source, overriddenTarget);
        assertEquals("CLI", new ObjectMapper().readTree(Files.readAllBytes(overriddenTarget)).get(0).get("name").asText());
    }
}

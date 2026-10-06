package com.universalconverter.config;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.*;

class YamlConfigLoaderTest {
    @Test void loadsConversionAndOptions() {
        var config = new YamlConfigLoader().load(new ByteArrayInputStream("source: input.csv\ntarget: output.json\ntype: csv-to-json\noptions:\n  delimiter: ';'\n  header: true\n".getBytes()));
        assertEquals("input.csv", config.source()); assertEquals(";", config.optionAsString("delimiter", ",")); assertTrue(config.optionAsBoolean("header", false));
    }
}

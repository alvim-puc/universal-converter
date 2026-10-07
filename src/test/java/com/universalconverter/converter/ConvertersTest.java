package com.universalconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalconverter.core.ConversionConfig;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ConvertersTest {
    private static final ConversionConfig CSV = new ConversionConfig("in", "out", "test", Map.of("delimiter", ",", "header", true));
    @Test void convertsCsvToJson() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); new CsvToJsonConverter().convert(bytes("name,age\nAna,24\n"), out, CSV);
        var tree = new ObjectMapper().readTree(out.toByteArray()); assertEquals("Ana", tree.get(0).get("name").asText()); assertEquals("24", tree.get(0).get("age").asText());
    }
    @Test void convertsJsonToCsv() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); new JsonToCsvConverter().convert(bytes("[{\"name\":\"Ana\",\"age\":24}]"), out, CSV);
        assertEquals("name,age\r\nAna,24\r\n", out.toString());
    }
    @Test void convertsXmlToJson() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); new XmlToJsonConverter().convert(bytes("<person><name>Ana</name></person>"), out, CSV);
        assertEquals("Ana", new ObjectMapper().readTree(out.toByteArray()).get("name").asText());
    }
    @Test void convertsYamlToJson() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); new YamlToJsonConverter().convert(bytes("name: Ana\nage: 24\n"), out, CSV);
        assertEquals(24, new ObjectMapper().readTree(out.toByteArray()).get("age").asInt());
    }
    @Test void convertsYamlAndTomlBothWays() throws Exception {
        ByteArrayOutputStream toml = new ByteArrayOutputStream(); new YamlToTomlConverter().convert(bytes("name: Ana\ncount: 2\n"), toml, CSV);
        assertTrue(toml.toString().contains("name = \"Ana\""));
        ByteArrayOutputStream yaml = new ByteArrayOutputStream(); new TomlToYamlConverter().convert(new ByteArrayInputStream(toml.toByteArray()), yaml, CSV);
        assertTrue(yaml.toString().contains("count: 2"));
    }
    @Test void convertsJsonToYaml() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); new JsonToYamlConverter().convert(bytes("{\"name\":\"Ana\",\"age\":24}\n"), out, CSV);
        assertTrue(out.toString().contains("name: Ana"));
        assertTrue(out.toString().contains("age: 24"));
    }
    private static ByteArrayInputStream bytes(String value) { return new ByteArrayInputStream(value.getBytes()); }
}

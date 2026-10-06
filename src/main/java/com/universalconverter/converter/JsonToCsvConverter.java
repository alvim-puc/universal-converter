package com.universalconverter.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class JsonToCsvConverter implements Converter {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override public String type() { return "json-to-csv"; }
    @Override public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException {
        JsonNode rows = mapper.readTree(input);
        if (!rows.isArray()) throw new IllegalArgumentException("JSON input must be an array of objects");
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        for (JsonNode row : rows) { if (!row.isObject()) throw new IllegalArgumentException("Each JSON array item must be an object"); row.fieldNames().forEachRemaining(headers::add); }
        CSVFormat.Builder format = CSVFormat.DEFAULT.builder().setDelimiter(delimiter(config));
        if (config.optionAsBoolean("header", true)) format.setHeader(headers.toArray(String[]::new));
        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output, StandardCharsets.UTF_8), format.build())) {
            for (JsonNode row : rows) {
                List<String> values = new ArrayList<>();
                for (String header : headers) { JsonNode value = row.get(header); values.add(value == null || value.isNull() ? "" : value.isValueNode() ? value.asText() : mapper.writeValueAsString(value)); }
                printer.printRecord(values);
            }
        }
    }
    private static char delimiter(ConversionConfig config) { String value = config.optionAsString("delimiter", ","); if (value.length() != 1) throw new IllegalArgumentException("CSV delimiter must be one character"); return value.charAt(0); }
}

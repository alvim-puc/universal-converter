package com.universalconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class CsvToJsonConverter implements Converter {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override public String type() { return "csv-to-json"; }
    @Override public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException {
        boolean header = config.optionAsBoolean("header", true);
        CSVFormat.Builder builder = CSVFormat.DEFAULT.builder().setDelimiter(delimiter(config));
        if (header) builder.setHeader().setSkipHeaderRecord(true);
        try (CSVParser parser = builder.build().parse(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            List<Map<String, String>> records = new ArrayList<>();
            for (CSVRecord record : parser) {
                Map<String, String> row = new LinkedHashMap<>();
                if (header) parser.getHeaderMap().keySet().forEach(column -> row.put(column, record.get(column)));
                else for (int i = 0; i < record.size(); i++) row.put("column" + (i + 1), record.get(i));
                records.add(row);
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(output, records);
        }
    }
    private static char delimiter(ConversionConfig config) {
        String value = config.optionAsString("delimiter", ",");
        if (value.length() != 1) throw new IllegalArgumentException("CSV delimiter must be one character");
        return value.charAt(0);
    }
}

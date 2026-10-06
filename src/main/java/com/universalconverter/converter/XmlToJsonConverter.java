package com.universalconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.universalconverter.core.ConversionConfig;
import com.universalconverter.core.Converter;
import java.io.*;

public final class XmlToJsonConverter implements Converter {
    private final XmlMapper xml = new XmlMapper(); private final ObjectMapper json = new ObjectMapper();
    @Override public String type() { return "xml-to-json"; }
    @Override public void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException { json.writerWithDefaultPrettyPrinter().writeValue(output, xml.readTree(input)); }
}

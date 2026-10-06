package com.universalconverter.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Converter {
    String type();
    void convert(InputStream input, OutputStream output, ConversionConfig config) throws IOException;
}

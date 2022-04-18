package gunten.spi.example.strategy;


import gunten.spi.example.Constants;
import gunten.spi.example.Parser;

import java.io.File;

/**
 **/
public class XmlParser implements Parser {
    public String parse(File file) throws Exception {
        return "我是XML解析方式";
    }

    public String getType() {
        return Constants.XML_PARSER;
    }
}

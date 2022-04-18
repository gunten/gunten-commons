package gunten.spi.example.strategy;


import gunten.spi.example.Constants;
import gunten.spi.example.Parser;

import java.io.File;

/**
 **/
public class JsonParser implements Parser {
    public String parse(File file) throws Exception {
        //TODO
        return "我是Json格式解析";
    }

    public String getType() {
        return Constants.JSON_PARSER;
    }
}

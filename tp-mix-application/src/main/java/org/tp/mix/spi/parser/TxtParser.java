package org.tp.mix.spi.parser;


import gunten.spi.example.Parser;

import java.io.File;

/**
 **/
public class TxtParser implements Parser {
    @Override
    public String parse(File file) throws Exception {
        return "我是基于txt解析";
    }

    @Override
    public String getType() {
        return "txt";
    }
}

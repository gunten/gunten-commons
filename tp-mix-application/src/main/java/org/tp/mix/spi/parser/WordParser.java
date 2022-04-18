package org.tp.mix.spi.parser;


import gunten.spi.example.Parser;

import java.io.File;

/**
 **/
public class WordParser implements Parser {
    @Override
    public String parse(File file) throws Exception {
        return "我是基于word解析";
    }

    @Override
    public String getType() {
        return "word";
    }
}

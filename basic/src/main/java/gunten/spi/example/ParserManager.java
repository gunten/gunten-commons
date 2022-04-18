package gunten.spi.example;


import gunten.spi.example.strategy.JsonParser;
import gunten.spi.example.strategy.XmlParser;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 **/
public class ParserManager {

    private final static ConcurrentHashMap<String, Parser> registeredParser=new ConcurrentHashMap<String, Parser>();

    static {
        loadInitialParser();
        initDefaultStrategy();
    }

    //SPI的加载
    private static void loadInitialParser(){
        //SPI的api方法
        //加载META-INF/service目录下 Parser的实现类
        ServiceLoader<Parser> parserServiceLoader=ServiceLoader.load(Parser.class);
        Iterator<Parser> iterator = parserServiceLoader.iterator();
        while(iterator.hasNext()){
            Parser parser=iterator.next();
            registeredParser.put(parser.getType(),parser);
        }
    }
    private static void initDefaultStrategy(){
        Parser jsonParser=new JsonParser();
        Parser xmlParser=new XmlParser();
        registeredParser.put(jsonParser.getType(),jsonParser);
        registeredParser.put(xmlParser.getType(),xmlParser);
    }
    public static Parser getParser(String key){
        return registeredParser.get(key);
    }
}

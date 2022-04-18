package gunten.spi.example;

import java.io.File;

/**
 **/
public interface Parser {

    /**
     * 解析文件的逻辑
     * @param file
     * @return
     * @throws Exception
     */
    String parse(File file) throws Exception;

    //文件类型
    String getType();
}

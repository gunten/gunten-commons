package org.tp.forest;

import com.dtflys.forest.annotation.Request;

/**
 * @author gunten
 * 2022/4/22
 */

public interface MyForestClient {

    @Request(url="http://localhost:8080/parser/{0}")
    String helloForest(String type);

}

package org.tp.forest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * 2022/4/22
 */
@Component
public class MyService {
    @Autowired
    private MyForestClient forestClient;

    public String testClient() {
        String result = forestClient.helloForest("txt");
        System.out.println(result);
        return result;
    }

}

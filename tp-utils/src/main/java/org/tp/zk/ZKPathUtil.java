package org.tp.zk;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * ZK路径处理工具
 * 2018-09-19 下午5:52
 **/
public class ZKPathUtil {

    /**
     * 把路径分词到list
     * @param path
     * @return
     */
    public static List<String> analysisZkPath(String path) {
        boolean b = StringUtils.isEmpty(path);
        if (b) {
            throw new NullPointerException("path is null !");
        }
        List<String> list = Splitter.on("/").omitEmptyStrings().trimResults().splitToList(path);
        return list;
    }

    public static String createZkPath(List<String> list){
        String s = "";
        if (list.size() > 1) {
            list = list.subList(0, list.size() - 1);
            s = "/" + Joiner.on("/").join(list);
        }
        return s;
    }

}

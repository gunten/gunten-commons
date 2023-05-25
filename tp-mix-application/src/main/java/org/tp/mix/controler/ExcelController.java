package org.tp.mix.controler;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tp.excel.poi.PoiUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/excel")
public class ExcelController {

    private Map<String, Double> getDataMap() {
        Map<String, Double> dataMap = new HashMap<>();
        dataMap.put("todo_1", 10d);
        dataMap.put("todo_2", 20d);
        dataMap.put("todo_3", 30d);
        dataMap.put("todo_4", 40d);
        dataMap.put("todo_5", 50d);
        return dataMap;
    }

    private Map<String, ImmutablePair<Double, String>> getDataMap2() {
        Map<String, ImmutablePair<Double, String>> dataMap = new HashMap<>();
        dataMap.put("todo_1", new ImmutablePair<>(10d, "{\"name\": \"zhangsan\" , \"age\" :22}"));
        dataMap.put("todo_2", new ImmutablePair<>(20d, "{\"name\": \"lisi\" , \"age\" :22}"));
        dataMap.put("todo_3", new ImmutablePair<>(30d, "{\"name\": \"wangwu\" , \"age\" :22}"));
        dataMap.put("todo_4", new ImmutablePair<>(40d, "{\"name\": \"zhaoliu\" , \"age\" :22}"));
        dataMap.put("todo_5", new ImmutablePair<>(50d, "{\"auto\": null}"));
        return dataMap;
    }

    private Map<String, ImmutablePair<Boolean, String>> getDataMap3() {
        Map<String, ImmutablePair<Boolean, String>> dataMap = new HashMap<>();
        dataMap.put("todo_1", new ImmutablePair<Boolean, String>(null, "{\"todo\":\"todo_11\"}"));
        dataMap.put("todo_2", new ImmutablePair<Boolean, String>(false, "{\"todo\":\"todo_22\"}"));
        dataMap.put("todo_3", new ImmutablePair<Boolean, String>(Boolean.TRUE, "{\"todo\":\"todo_33\"}"));
        dataMap.put("todo_4", new ImmutablePair<Boolean, String>(null, "{\"todo\":\"todo_44\"}"));
        dataMap.put("todo_5", new ImmutablePair<Boolean, String>(null, "{\"todo\":\"todo_55\"}"));
        return dataMap;
    }

    private Map<String, ImmutablePair<String, String>> getDataMap4() {
        Map<String, ImmutablePair<String, String>> dataMap = new HashMap<>();
        dataMap.put("todo_1", new ImmutablePair<String, String>(null, "{\"todo\":\"todo_11\"}"));
        dataMap.put("todo_2", new ImmutablePair<String, String>("hahaha", "{\"todo\":\"todo_22\"}"));
        dataMap.put("todo_3", new ImmutablePair<String, String>(null, "{\"todo\":\"todo_33\"}"));
        dataMap.put("todo_4", new ImmutablePair<String, String>(null, "{\"todo\":\"todo_44\"}"));
        dataMap.put("todo_5", new ImmutablePair<String, String>("某个公式", "{\"todo\":\"todo_55\"}"));
        return dataMap;
    }

    @GetMapping("/toHtml")
    public void toHtml(HttpServletResponse response) throws Exception {
        String path = this.getClass().getClassLoader().getResource("excel1_1.xlsx").getPath();
        PoiUtils.templateToExcelHtml(new FileInputStream(path), getDataMap3(), false, response);
    }

    @RequestMapping("/show")
    public void show(HttpServletResponse response) throws IOException {
        String path = this.getClass().getClassLoader().getResource("excel1_1.xlsx").getPath();
//        PoiUtils.templateToExcelHtml(new FileInputStream(path), getDataMap2(), true, response);
        PoiUtils.templateToExcelHtml(new FileInputStream(path), getDataMap4(), true, response);
    }

    @RequestMapping("/export")
    public void export (HttpServletResponse response) throws IOException {
        String path = this.getClass().getClassLoader().getResource("excel1_1.xlsx").getPath();
        PoiUtils.templateToExcel(new FileInputStream(path), "excel1_1新文件", getDataMap(), response);
    }



}

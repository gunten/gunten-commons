package org.tp.controler;

import cn.hutool.core.util.ZipUtil;
import gunten.spi.example.ParserManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  SPI 测试入口
 **/
@RestController
@RequestMapping("/test")
public class TestController {



    /**
     * 从MinIO下载多个文件
     *
     * @param response
     */
    @GetMapping("/downloadZip")
    public void downloadZip(HttpServletResponse response) throws IOException {
        List<String> fileUrlList = new ArrayList<>(Arrays.asList("D:\\桌面\\生产日报基础数据-2024年06月02日.xlsx","D:\\桌面\\配煤表-2024年06月02日.xlsx"));
        //被压缩文件InputStream
        InputStream[] srcFiles = new InputStream[fileUrlList.size()];
        //被压缩文件名称
        String[] srcFileNames = new String[fileUrlList.size()];
        for (int i = 0; i < fileUrlList.size(); i++) {
            String fileUrl = fileUrlList.get(i);
            InputStream inputStream = Files.newInputStream(Paths.get(fileUrl));
            if (inputStream == null) {
                continue;
            }
            srcFiles[i] = inputStream;

            String separator = File.separator;
            String[] splitFileUrl = fileUrl.split("\\" + separator);
            srcFileNames[i] = splitFileUrl[splitFileUrl.length - 1];
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("下载测试.zip", "UTF-8"));
        //多个文件压缩成压缩包返回
        ZipUtil.zip(response.getOutputStream(), srcFileNames, srcFiles);
    }

    public static void main(String[] args) {
        String fileUrl = "D:\\桌面\\生产日报基础数据-2024年06月02日.xlsx";
        String separator = File.separator;
        String[] splitFileUrl = fileUrl.split("\\" + separator);
        Arrays.stream(splitFileUrl).forEach(System.out::println);
    }
}

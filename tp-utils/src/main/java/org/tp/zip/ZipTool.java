package org.tp.zip;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 压缩包处理工具
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/5/31
 */
@Slf4j
public class ZipTool {

    private ZipTool() {
    }

    /**
     * 解压zip文件
     * @param zip zip文件位置
     * @param outputDir 解压缩后文件保存路径
     * @param charsetName 字符编码
     */
    public static void unpackZip(File zip, File outputDir, String charsetName) throws IOException {

        InputStream in;
        Charset charset = StringUtils.isNotBlank(charsetName) ?
                Charset.forName(charsetName) : Charset.forName("utf8");

        try (ZipFile zipFileData = new ZipFile(zip.getPath(), charset)){
            //若目标保存文件位置不存在
            if (outputDir != null && !outputDir.exists()) {
                outputDir.mkdirs();
            }

            //读出文件数据
            Enumeration<? extends ZipEntry> entriesData = zipFileData.entries();
            while (entriesData.hasMoreElements()) {
                ZipEntry entry = entriesData.nextElement();
                in = zipFileData.getInputStream(entry);
                String filePath;
                if (outputDir == null) {
                    filePath = zip.getParentFile().getPath() + File.separator + entry.getName();
                } else {
                    filePath = outputDir.getPath() + File.separator + entry.getName();
                }
                File file = new File(filePath);
                if (file.isDirectory()) {
                    continue;
                }
                try (OutputStream out = new FileOutputStream(filePath)) {
                    int len = -1;
                    byte[] bytes = new byte[1024];
                    while ((len = in.read(bytes)) != -1) {
                        out.write(bytes, 0, len);
                    }
                    out.flush();
                }
            }


        } catch (Exception e) {
            throw e;
        }
    }

}

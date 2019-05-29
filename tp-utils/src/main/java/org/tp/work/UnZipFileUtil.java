package org.tp.work;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 非纯粹解压工具 TODO 整理
 * 实现文件[夹]解压 
 * @author ljheee 
 * 
 */
@Slf4j
public class UnZipFileUtil {
    /**
     * 解压到指定目录 
     * @param zipPath   
     * @param descDir  
     */
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        unZipFiles(new File(zipPath), descDir);
    }

    /** 
     * 解压文件到指定目录 
     * 解压后的文件名，和之前一致 
     * @param zipFile   待解压的zip文件 
     * @param descDir   指定目录 
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) throws IOException {

        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));//解决中文文件夹乱码  
        try {
            String name = zip.getName().substring(zip.getName().lastIndexOf('\\') + 1, zip.getName().lastIndexOf('.'));

            File pathFile = new File(descDir + name);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                try {
                    String outPath = (descDir + name + "/" + zipEntryName).replaceAll("\\*", "/");

                    // 判断路径是否存在,不存在则创建文件路径  
                    File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
                    if (new File(outPath).isDirectory()) {
                        continue;
                    }
                    // 输出文件路径信息  
                    //          System.out.println(outPath);  
                    try 
                    (FileOutputStream out = new FileOutputStream(outPath);
                     InputStream in = zip.getInputStream(entry);)
                    {
                        byte[] buf1 = new byte[1024];
                        int len;
                        while ((len = in.read(buf1)) > 0) {
                            out.write(buf1, 0, len);
                        }
                    }catch (Exception e) {
                        log.error("", e);
                    }

                } catch (Exception e) {
                    log.error("", e);
                }finally {
                    
                }
            }
        } catch (Exception e) {
            log.error("context:",e.toString());
        }finally{
            if(zip != null){
                zip.close();
            }
        }
        System.out.println("******************解压完毕********************");
        return;
    }

    /**
     * 读取某个文件夹下的所有文件
     * @throws Exception
     */
    public static boolean readfile(String filepath, String path) throws Exception {
        try {
            InputStream inputStream = null;
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("path=" + file.getPath());
                unZip(file.getPath(), inputStream,file.getName(),path);
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        System.out.println("path=" + readfile.getPath());
                        unZip(readfile.getPath(), inputStream,readfile.getName(),path);
                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i], path);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }

    public static void unZip(String zipPath, InputStream inputStream,String fileName,String path) {
        try {
            //解密  解压
            File file = new File(zipPath);
            if (file.isFile() && file.exists()) {
                InputStream inputStreamNew = new FileInputStream(zipPath);
                inputStreamNew = ZipUtil.decryptMode(FILE_DES3_KEY, inputStreamNew);//解密

                ZipEntry entry = null;
                try (ZipInputStream zis = new ZipInputStream(inputStreamNew);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zis, "gbk"));){
                    while ((entry = zis.getNextEntry()) != null) {
                        if (entry.isDirectory()) {
                            zis.closeEntry();
                            continue;
                        }
                        String lineTxt = null;

                        fileName = fileName.substring(0,fileName.lastIndexOf("."));
                        File fileTxt = new File(path+fileName+".txt");
                        if(!file.exists()){
                            file.getParentFile().mkdirs();
                        }
					boolean result = file.createNewFile();                        // write
                        try (FileWriter fw = new FileWriter(fileTxt, true);
                             BufferedWriter bw = new BufferedWriter(fw);)
                        {
                            while ((lineTxt = bufferedReader.readLine()) != null){
                                System.out.println(lineTxt);
                                bw.write(lineTxt);
                                bw.newLine();
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        } catch (IOException e) {
            log.info("context:",e.toString());
        }
    }

//    public static void ZipFile(ZipOutputStream zos, File file, String fileName) throws Exception {
//        if (file.isDirectory()) {
//            //创建压缩文件的目录结构
//            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName)) + File.separator));
//
//            for (File f : file.listFiles()) {
//                ZipFile(zos, f, fileName);
//            }
//        } else {
//            //打印输出正在压缩的文件
//            System.out.println("正在压缩文件:" + file.getName());
//            //创建压缩文件
//            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))));
//
//            try (   //用字节方式读取源文件
//                    InputStream is = new FileInputStream(file.getPath());
//                    //创建一个缓存区
//                    BufferedInputStream bis = new BufferedInputStream(is);)
//            {
//                //字节数组,每次读取1024个字节
//                byte[] b = new byte[1024];
//                //循环读取，边读边写
//                while (bis.read(b) != -1) {
//                    zos.write(b);//写入压缩文件
//                }
//            } catch (Exception e) {
//                log.error("", e);
//            }
//        }
//    }

    private static final String FILE_DES3_KEY = "192837464637281964738291";

    /**
     * 先根遍历序递归删除文件夹
     *
     * @param dirFile 要被删除的文件或者目录
     * @return 删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }
    
    /** 
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下 
     * @param sourceFilePath :待压缩的文件路径 
     * @param zipFilePath :压缩后存放路径 
     * @param fileName :压缩后文件的名称 
     * @return 
     */  
    public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){  
        boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
           
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");  
        }else{  
            try {  
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
                if(zipFile.exists()){  
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
                    deleteFile(zipFile);
                }

                File[] sourceFiles = sourceFile.listFiles();
                if(null == sourceFiles || sourceFiles.length<1){
                    System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                }else{

                    try (FileOutputStream fos = new FileOutputStream(zipFile);
                         ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));  )
                    {
                        byte[] bufs = new byte[1024*10];
                        for(int i=0;i<sourceFiles.length;i++){
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里

                            try (   FileInputStream fis = new FileInputStream(sourceFiles[i]);
                                    BufferedInputStream bis = new BufferedInputStream(fis, 1024*10);  )
                            {
                                int read = 0;
                                while((read=bis.read(bufs, 0, 1024*10)) != -1){
                                    zos.write(bufs,0,read);
                                }
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }
                        flag = true;
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            } 
        }  
        return flag;  
    }  
    
    //测试  
    public static void main(String[] args) throws Exception {
        //压缩文件内的文件夹名称
        String name="CJS";
		String fileName = "E:\\baosong\\CJS20181107.zip";
		File file = new File(fileName);
        if(!file.exists()){
            System.out.println("文件不存在"+fileName);
            return ;
        }
        try {
            unZipFiles(new File(fileName), file.getParent()+"\\");
            String newName = file.getName().substring(0, file.getName().lastIndexOf("."));
            File fileNew = new File(file.getParent()+"\\"+name+"\\");
            if(fileNew.exists()){
                boolean result = fileNew.delete();
                if(!result){
                    log.info("文件删除异常:"+fileName);
                }
            }
            fileNew.mkdirs();
            readfile(file.getParent()+"\\"+newName+"\\"+name+"\\", file.getParent()+"\\"+name+"\\");//解压加密文件，读取生成新文件
            deleteFile(new File(file.getParent()+"//"+newName));//删除加密文件
            fileToZip(file.getParent()+"\\"+name+"\\", file.getParent(), name);//压缩生成新文件
            deleteFile(new File(file.getParent()+"//"+name));//删除生成新文件
        } catch (Exception e) {
            log.info("context:",e.toString());
        } 
    }

}
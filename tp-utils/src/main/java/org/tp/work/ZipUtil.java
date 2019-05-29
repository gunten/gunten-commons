package org.tp.work;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;

/**
 * 解压缩zip包
 */
public class ZipUtil {
	private static final String ALGORITHM = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish        //keybyte为加密密钥，长度为24字节    //src为被加密的数据缓冲区（源）    
    private final static Logger logger = LoggerFactory.getLogger(ZipUtil.class);


    /**
     * 递归压缩文件夹
     * @param srcRootDir 压缩文件夹根目录的子路径
     * @param file 当前递归压缩的文件或目录对象
     * @param zos 压缩文件存储对象
     * @throws Exception
     */
    private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception {
        if (file == null) {
            return;
        }

        //如果是文件，则直接压缩该文件
        if (file.isFile()) {
            int count, bufferLen = 1024;
            byte data[] = new byte[bufferLen];

            //获取文件相对于压缩文件夹根目录的子路径
            String subPath = file.getAbsolutePath();
            int index = subPath.indexOf(srcRootDir);
            if (index != -1) {
                subPath = subPath.substring(srcRootDir.length() + File.separator.length());
            }
            ZipEntry entry = new ZipEntry(subPath);
            zos.putNextEntry(entry);
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));)
            {
                while ((count = bis.read(data, 0, bufferLen)) != -1) {
                    zos.write(data, 0, count);
                }
            } catch (Exception e) {
                logger.info("context:",e.toString());
            }
            zos.closeEntry();
        }
        //如果是目录，则压缩整个目录
        else {
            //压缩目录中的文件或子目录
            File[] childFileList = file.listFiles();
            for (int n = 0; n < childFileList.length; n++) {
                int result = childFileList[n].getAbsolutePath().indexOf(file.getAbsolutePath());
                zip(srcRootDir, childFileList[n], zos);
            }
        }
    }

    /**
     * 对文件或文件目录进行压缩
     * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
     * @param zipPath 压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
     * @param zipFileName 压缩文件名
     * @throws Exception
     */
    public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath) || StringUtils.isEmpty(zipFileName)) {
            return;
        }
        try{
            File srcFile = new File(srcPath);

            //判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
            if (srcFile.isDirectory() && zipPath.indexOf(srcPath) != -1) {
                return;
            }

            //判断压缩文件保存的路径是否存在，如果不存在，则创建目录
            File zipDir = new File(zipPath);
            if (!zipDir.exists() || !zipDir.isDirectory()) {
                zipDir.mkdirs();
            }

            //创建压缩文件保存的文件对象
            String zipFilePath = zipPath + File.separator + zipFileName;
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
                SecurityManager securityManager = new SecurityManager();
                securityManager.checkDelete(zipFilePath);
                //删除已存在的目标文件
                boolean result = zipFile.delete();
                if(!result){
                    logger.info("文件删除异常:"+zipFileName);
                }
            }

            //如果只是压缩一个文件，则需要截取该文件的父目录
            String srcRootDir = srcPath;
            if (srcFile.isFile()) {
                int index = srcPath.lastIndexOf(File.separator);
                if (index != -1) {
                    srcRootDir = srcPath.substring(0, index);
                }
            }

            //调用递归压缩方法进行目录或文件压缩
          try  (CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
                ZipOutputStream zos = new ZipOutputStream(cos);)
            {
                zip(srcRootDir, srcFile, zos);
            } catch (Exception e) {
                logger.info("context:",e.toString());
            }
        } catch (Exception e) {
            logger.info("context:",e.toString());
        }
    }

    /**
     * 解压缩zip包
     * @param zipFilePath zip文件的全路径
     * @param unzipFilePath 解压后的文件保存的路径
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含
     * @return 返回解压后的文件名集合
     */
    @SuppressWarnings({ "unchecked", "resource" })
    public static List<String> unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
            //            throw new ParameterException(ICommonResultCode.PARAMETER_IS_NULL);          
        }
        File zipFile = new File(zipFilePath);
        //如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
        if (includeZipFileName) {
            String fileName = zipFile.getName();
            if (StringUtils.isNotEmpty(fileName)) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            unzipFilePath = unzipFilePath + File.separator + fileName;
        }
        //创建解压缩文件保存的路径
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
            unzipFileDir.mkdirs();
        }

        //开始解压
        ZipEntry entry = null;
        String entryFilePath = null, entryDirPath = null;
        File entryFile = null, entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        List<String> fileNames = new ArrayList<>();
        try (ZipFile zip = new ZipFile(zipFile);)
        {
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
            fileNames = new ArrayList<String>();
            //循环对压缩包里的每一个文件进行解压     
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                //构建压缩包中一个文件解压后保存的文件全路径
                entryFilePath = unzipFilePath + File.separator + entry.getName();
                //构建解压后保存的文件夹路径
                index = entryFilePath.lastIndexOf(File.separator);
                if (index != -1) {
                    entryDirPath = entryFilePath.substring(0, index);
                } else {
                    entryDirPath = "";
                }
                entryDir = new File(entryDirPath);
                //如果文件夹路径不存在，则创建文件夹
                if (!entryDir.exists() || !entryDir.isDirectory()) {
                    entryDir.mkdirs();
                }
            //创建解压文件
            entryFile = new File(entryFilePath);
            if (entryFile.exists()) {
                //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
                //                SecurityManager securityManager = new SecurityManager();
                //                securityManager.checkDelete(entryFilePath);
                //删除已存在的目标文件
              boolean result =  entryFile.delete();
              if(!result){
                  logger.info("文件删除异常:"+entryFile.getName());
              }
            }
                //写入文件
                try (   BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                        BufferedInputStream bis = new BufferedInputStream(zip.getInputStream(entry));)
                {
                    while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                        bos.write(buffer, 0, count);
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
                fileNames.add(entry.getName());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return fileNames;
    }

    /**
     * 数据压缩传输
     * @param bytes
     * @param out
     * @throws IOException
     */
    public static void compressTransfe(byte[] bytes, OutputStream out) throws IOException {
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(out);
            gos.write(bytes);
            gos.finish();
            gos.flush();
        } finally {
            if (gos != null) {
                gos.close();
            }
        }
    }

    /**
     * 数据压缩
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = null;
        GZIPOutputStream gos = null;
        try {
            out = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(out);
            gos.write(bytes);
            gos.finish();
            gos.flush();
        } finally {
            if (gos != null) {
                gos.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return out.toByteArray();
    }

    /**
     * 数据解压
     * @param bytes
     * @return
     * @throws IOException
     */
    public static byte[] decompress(byte[] bytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gin = new GZIPInputStream(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int count;
        byte data[] = new byte[1024];
        while ((count = gin.read(data, 0, 1024)) != -1) {
            out.write(data, 0, count);
        }
        out.flush();
        out.close();
        gin.close();
        return out.toByteArray();
    }
    
    
    /**
     * 设置数据流为解密模式
     * @param des3Key
     * @param inputStream
     * @return
     */
    public static CipherInputStream decryptMode(String des3Key, InputStream inputStream) {
        //生成密钥          
        SecretKey deskey = new SecretKeySpec(des3Key.getBytes(), ALGORITHM); //解密         
        return decryptMode(deskey, inputStream);
    }
    
    /**
     * 设置数据流为解密模式
     * @param secretKey
     * @param inputStream
     * @return
     */
    public static CipherInputStream decryptMode(SecretKey secretKey, InputStream inputStream) {
        try {
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, secretKey);
            return new CipherInputStream(inputStream, c1);
        } catch (InvalidKeyException e) {
            logger.info("context:",e.toString());
        } catch (NoSuchAlgorithmException e) {
            logger.info("context:",e.toString());
        } catch (NoSuchPaddingException e) {
            logger.info("context:",e.toString());
        }
        return null;
    }

    /**
     * 使用DESede算法加密文件
     * @param scrPath 源路径
     * @param targetPath 目标路径
     */
    public static void encrypt(String scrPath,String targetPath,String key) throws Exception {
        FileInputStream fileInputStream = null;
        CipherInputStream cipherInputStream = null;
        try {
            //获取源文件
            fileInputStream = new FileInputStream(scrPath);
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            SecretKey deskey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            //加密文件
            cipherInputStream = new CipherInputStream(fileInputStream, c1);
            //保存加密后的文件
            FileUtils.copyInputStreamToFile(cipherInputStream,new File(targetPath));
        } finally {
            //关闭流
            IOUtils.closeQuietly(cipherInputStream);
            IOUtils.closeQuietly(fileInputStream);
        }
    }

}

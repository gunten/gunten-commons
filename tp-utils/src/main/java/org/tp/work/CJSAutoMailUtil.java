package org.tp.work;

import org.tp.mail.JavaMailUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


/**
 * {@link CJSAutoMailUtil}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see CJSAutoMailUtil
 * 2018/11/7
 */
public class CJSAutoMailUtil {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("please put mailPassword like: java  JavaMailUtilTest  pwd");
            System.out.println("password:");
            Scanner sc = new Scanner(System.in);
            JavaMailUtil.myEmailPassword = sc.nextLine();
        }else{
            JavaMailUtil.myEmailPassword = args[0];
        }

        char separator;
        if( System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0 )
            separator = '\\';
        else
            separator = '/';

        //压缩文件内的文件夹名称
        String name="CJS";
        File directory = new File("");//设定为当前文件夹

        try {
            String decodeDir = directory.getCanonicalPath()+separator +"toSend"+separator;
            File f = new File(decodeDir);
            if(!f.exists()){
                f.mkdirs();
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            //解压加密文件，读取生成新文件
            UnZipFileUtil.readfile(directory.getCanonicalPath()+separator+"CJS"+separator, decodeDir);
            UnZipFileUtil.fileToZip( decodeDir, directory.getCanonicalPath(), name+sf.format(new Date()));//压缩生成新文件
            UnZipFileUtil.deleteFile(new File(decodeDir));//删除生成新文件

            //liuguoqing
            JavaMailUtil.receiveMailAccount = "10786@etransfar.com";
            String fileName = name+sf.format(new Date())+".zip";
            String[] files = new String[]{directory.getCanonicalPath()+separator+fileName};
            try {
                JavaMailUtil.sendMail("",fileName,"",files);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("context:" + e.toString());
        }
    }
}

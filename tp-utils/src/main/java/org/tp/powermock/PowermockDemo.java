package org.tp.powermock;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link PowermockDemo}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see PowermockDemo
 * 2018/11/21
 */
@Slf4j
public class PowermockDemo extends Messages{

    private String path;

    public boolean callArgumentInstance(File file) {
        return file.exists();
    }


    public String getFilePath() {
        return path;
    }
    public String getPayloadName() {
        String pathWithName = getFilePath();
        try {
            int index = pathWithName.lastIndexOf("Citrix");
            return pathWithName.substring(index);
        }
        catch (Exception e) {
            return pathWithName;
        }
    }


    public boolean callInternalInstance(String path) {
        File file = new File(path);
        return file.exists();
    }


    public boolean callFinalMethod(Dependency d){
        return d.isAlive();
    }

    public boolean callPrivateMethod(){
        return isAlive();
    }
    private boolean isAlive(){
        return true;
    }


    public boolean callStaticMethod(){
        return Dependency.isExist();
    }

    private static final String MSG_DOWNLOAD_FAILED = Messages.getString("RefreshThread.0");
    public boolean downloadFiles(String path) {
        return download(path);
    }
    private boolean download(String localPath){
        return false;
    }



    private boolean isAvailable = true;
    private List<FileItem> items = new ArrayList<>();

    public boolean isAvailable() {
        return isAvailable;
    }

    private Date parseDate(String date) {
        if(!isAvailable)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            if (date == null || date.isEmpty())
                return null;
            return sdf.parse(date);
        }
        catch (ParseException e) {
            log.warn( "[{}][{}]", e, getClass());
            return null;
        }
    }

    public static void main(String[] args) {
        PowermockDemo demo = new PowermockDemo();
        System.out.println(demo.parseDate("11/22/2018"));
    }
}

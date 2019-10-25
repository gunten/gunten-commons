//package org.tp.mix.sftp;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.tp.sftp.SftpTemplate;
//import org.tp.mix.MixApplication;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
///**
// * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
// * 2019/3/14
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = MixApplication.class)
//public class SftpTest {
//
//
//    @Autowired
//    @Qualifier("uploadSftpTemplate")
//    private SftpTemplate uploadSftpTemplate;
//
//    @Autowired
//    @Qualifier("downloadSftpTemplate")
//    private SftpTemplate downloadSftpTemplate;
//
//    @Test
//    public void integrativeTest() throws IOException {
//
//        boolean upload ,download,delete;
//        upload = uploadSftpTemplate.upload(null, "test1.xml", "D:\\test1.xml");
//
//        File file = new File("D:\\temp\\mytest.xml");
//        OutputStream out = new FileOutputStream(file);
//        download = downloadSftpTemplate.downloadFile("test1.xml",out);
//        out.close();
//
//        delete = uploadSftpTemplate.delete("test1.xml");
//
//        Assert.assertTrue(upload);
//        Assert.assertTrue(download);
//        Assert.assertTrue(delete);
//
//    }
//}

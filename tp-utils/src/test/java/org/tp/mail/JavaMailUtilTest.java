package org.tp.mail;

import java.util.Scanner;

/**
 * {@link JavaMailUtilTest}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see JavaMailUtilTest
 * 2018/11/7
 */
public class JavaMailUtilTest {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("please put mailPassword like: java  JavaMailUtilTest  pwd");
            System.out.println("password:");
            Scanner sc = new Scanner(System.in);
            JavaMailUtil.myEmailPassword = sc.nextLine();
        }else{
            JavaMailUtil.myEmailPassword = args[0];
        }

        JavaMailUtil.receiveMailAccount = "64588136@qq.com";
        String[] files = new String[]{"E:\\E-book\\一个程序员的Java和C学习之路整理.png"};
        try {
            JavaMailUtil.sendMail("","主题1","",files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

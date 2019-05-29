package org.tp.powermock;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import java.io.File;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * {@link PowermockDemoTest}
 *
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * @version 1.0.0
 * @see PowermockDemoTest
 * 2018/11/21
 * TODO 待完善
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PowermockDemo.class,Dependency.class})
public class PowermockDemoTest {

    /**
     * 普通Mock不需要加@RunWith和@PrepareForTest注解
     */
    @Test
    public void testCallArgumentInstance() {
        File file = PowerMockito.mock(File.class);
        PowermockDemo underTest = new PowermockDemo();
        PowerMockito.when(file.exists()).thenReturn(true);
        Assert.assertTrue(underTest.callArgumentInstance(file));
    }


    /**
     * 当使用mock出来的对象去调用某个方法时，要对该方法使用thenCallRealMethod().
     * @throws Exception
     */
    @Test
    public void testGetPayloadName() throws Exception {
        PowermockDemo item = PowerMockito.mock(PowermockDemo.class);
        String filePath = "../../../test/updates/Citrix.ibr";
        PowerMockito.when(item.getFilePath()).thenReturn(filePath);
        PowerMockito.when(item, "getPayloadName").thenCallRealMethod();
        Assert.assertEquals("Citrix.ibr", item.getPayloadName());
    }

    /**
     * 当使用PowerMockito.whenNew方法时，必须加@PrepareForTest和@RunWith注解。
     * 注解@PrepareForTest里写的类是需要mock的new对象代码所在的类
     * @throws Exception
     */
    @Test
    public void testCallInternalInstance() throws Exception {
        File file = PowerMockito.mock(File.class);
        PowermockDemo underTest = new PowermockDemo();
        PowerMockito.whenNew(File.class).withArguments("bing").thenReturn(file);
        PowerMockito.when(file.exists()).thenReturn(true);
        Assert.assertTrue(underTest.callInternalInstance("bing"));
    }


    /**
     * 当需要mock final方法的时候，必须加@PrepareForTest和@RunWith注解,
     * @PrepareForTest里写的类是final方法所在的类。
     */
    @Test
    public void testCallFinalMethod() {
        Dependency depencency =  PowerMockito.mock(Dependency.class);
        PowermockDemo demo = new PowermockDemo();
        PowerMockito.when(depencency.isAlive()).thenReturn(true);
        Assert.assertTrue(demo.callFinalMethod(depencency));
    }

    /**
     *  和mock final方法一样，必须加@PrepareForTest和@RunWith注解,@PrepareForTest里写的类是private方法所在的类
     * @throws Exception
     */
    @Test
    public void testCallPrivateMethod() throws Exception {
        PowermockDemo underTest = PowerMockito.mock(PowermockDemo.class);
        PowerMockito.when(underTest.callPrivateMethod()).thenCallRealMethod();
        PowerMockito.when(underTest, "isAlive").thenReturn(true);
        Assert.assertTrue(underTest.callPrivateMethod());
    }

    /**
     * mock静态方法时需要加@PrepareForTest和@RunWith注解，@PrepareForTest注解中是静态方法所在的类。
     */
    @Test
    public void testCallStaticMethod() {
        PowerMockito.mockStatic(Dependency.class);
        PowermockDemo underTest = new PowermockDemo();
        PowerMockito.when(Dependency.isExist()).thenReturn(true);
        Assert.assertTrue(underTest.callStaticMethod());
    }


    /**

     PowerMockito.suppress(PowerMockito.constructor(BaseEntity.class, String.class,  Integer.class))表示禁用参数为String和Integer类型的BaseEntity构造方法。
     PowerMockito.suppress(PowerMockito.method(BaseEntity.class, "performAudit", String.class))表示禁用BaseEntity的performAudit方法。
     PowerMockito.suppress(PowerMockito.field(BaseEntity.class,"identifier"))：禁用identifier域。

     * @throws Exception
     */
    @Test
    public void testDownloadFiles() throws Exception {
//        PowerMockito.suppress(PowerMockito.method(Messages.class, "getString",  String.class));
//        PowerMockito.suppress(PowerMockito.field(PowermockDemo.class, "MSG_DOWNLOAD_FAILED"));
        PowermockDemo mgmt = PowerMockito.mock(PowermockDemo.class);
        PowerMockito.when(mgmt, "download", anyString()).thenReturn(true);
        PowerMockito.when(mgmt.downloadFiles(anyString())).thenCallRealMethod();
        Assert.assertTrue(mgmt.downloadFiles("C:/temp"));
    }


    /**
     Powermock提供了一个Whitebox的class，可以方便的绕开权限限制，可以get/set private属性，实现注入。也可以调用private方法。也可以处理static的属性/方法，根据不同需求选择不同参数的方法即可

         Whitebox.setInternalState(object, "fieldName", value)可以设置某个对象的某个field。
         Whitebox.getInternalState(object, "fieldName")获取某个对象的某个field的值。
         Whitebox.invokeMethod(object, methodName, para)可以调用私有方法，测试私有方法的返回值。
     */
    @Test
    public void testParseDate() throws Exception {
        PowermockDemo demo = new PowermockDemo();
        Whitebox.setInternalState(demo, "isAvailable", true);
        Assert.assertTrue(demo.isAvailable());
        // other things
        List<FileItem> items = Whitebox.getInternalState(demo, "items");
        Assert.assertTrue(items.size() == 0);

        Whitebox.invokeMethod(demo, "parseDate", "11/22/2018");
    }

}

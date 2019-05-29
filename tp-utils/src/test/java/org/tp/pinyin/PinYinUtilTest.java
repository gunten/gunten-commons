package org.tp.pinyin;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;

/**
 * 2018/11/2
 */
public class PinYinUtilTest {

    @Test
    public void isEnglishTest(){
        Assert.assertTrue(PinYinUtil.isEnglish("haoiASFT"));
        Assert.assertFalse(PinYinUtil.isEnglish("haoiASFT7879"));
        Assert.assertFalse(PinYinUtil.isEnglish("haoiASFT&#."));
        Assert.assertFalse(PinYinUtil.isEnglish("haoiASFT这是个测试"));
        Assert.assertFalse(PinYinUtil.isEnglish("haoiASFT這個測試"));
    }

    @Test
    public void getPinYin() {
        Assert.assertEquals(1, PinYinUtil.getPinYin('2').length);
        Assert.assertEquals("2", PinYinUtil.getPinYin('2')[0]);
        Assert.assertEquals(1, PinYinUtil.getPinYin('a').length);
        Assert.assertEquals("a", PinYinUtil.getPinYin('a')[0]);
        Assert.assertEquals(1, PinYinUtil.getPinYin('A').length);
        Assert.assertEquals("A", PinYinUtil.getPinYin('A')[0]);
        Assert.assertEquals(1, PinYinUtil.getPinYin('$').length);
        Assert.assertEquals("$", PinYinUtil.getPinYin('$')[0]);
        Assert.assertEquals(1, PinYinUtil.getPinYin('化').length);
        Assert.assertEquals("hua", PinYinUtil.getPinYin('化')[0]);
        Assert.assertEquals(3, PinYinUtil.getPinYin('着').length);
        Assert.assertTrue(Arrays.stream(PinYinUtil.getPinYin('着')).collect(toList()).containsAll(Arrays.asList("zhao", "zhuo","zhe")));
        Assert.assertEquals(2, PinYinUtil.getPinYin('傳').length);
        Assert.assertTrue(Arrays.stream(PinYinUtil.getPinYin('傳')).collect(toList()).containsAll(Arrays.asList("zhuan", "chuan")));

    }

    @Test
    public void getPinYinSet() {
        Assert.assertEquals("123", PinYinUtil.getPinYinSet("123").toArray(new String[0])[0]);
        Assert.assertEquals("abc", PinYinUtil.getPinYinSet("abc").toArray(new String[0])[0]);
        Assert.assertEquals("ABC", PinYinUtil.getPinYinSet("ABC").toArray(new String[0])[0]);
        Assert.assertEquals("A\\C", PinYinUtil.getPinYinSet("A\\C").toArray(new String[0])[0]);
        Assert.assertEquals(1, PinYinUtil.getPinYinSet("化学").size());
        Assert.assertTrue(PinYinUtil.getPinYinSet("化学").containsAll(Arrays.asList("huaxue")));
        Assert.assertTrue(PinYinUtil.getPinYinSet("傳話").containsAll(Arrays.asList("chuanhua","zhuanhua")));
    }
}

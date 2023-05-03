package org.tp.easy.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;


/**
 * 导出Excel Reference: https://blog.csdn.net/qq_34087560/article/details/79352448
 * @author gunten
 * 2018/2/10.
 * @param <T>
 */
public class ExportExcelUtil<T>{

    // 2007 版本以上 最大支持1048576行
    public  final static String  EXCEl_FILE_2007 = "2007";
    // 2003 版本 最大支持65536 行
    public  final static String  EXCEL_FILE_2003 = "2003";


    /**
     * <p>
     * 导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
     * </p>
     *
     * @param title 表格标题
     * @param dataset 数据集合
     * @param out 输出流
     * @param version 2003 或者 2007，不传时默认生成2003版本
     * @param firstLine 是否包含头部标题
     */
    public static<T> void exportExcel(String title, Collection<T> dataset, OutputStream out,String version,boolean firstLine) {
        if(StringUtils.isBlank(version) || EXCEL_FILE_2003.equals(version.trim())){
            exportExcel2003(title, dataset, out, "yyyy-MM-dd hh:mm:ss",firstLine);
        }else{
            exportExcel2007(title, dataset, out, "yyyy-MM-dd hh:mm:ss",firstLine);
        }
    }

    /**
     * <p>
     *
     * 以web流的方式导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
     * </p>
     * @param title
     * @param dataset
     * @param version
     */
    public static<T> void exportExcelInWeb(String title, Collection<T> dataset,String version) {
        OutputStream out = null;// 创建一个输出流对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();// 初始化HttpServletResponse对象
        try {
            out = response.getOutputStream();
            title = new String(title.getBytes("gb2312"), "ISO8859-1");//headerString为中文时转码
        } catch (IOException e) {
            e.printStackTrace();
        }
        if( version.equals(EXCEl_FILE_2007)){
            response.setHeader("Content-disposition", "attachment; filename=" + title + ".xlsx");// filename是下载的xls的名，建议最好用英文
        }else{
            response.setHeader("Content-disposition", "attachment; filename=" + title + ".xls");// filename是下载的xls的名，建议最好用英文
        }
        response.setContentType("application/msexcel;charset=GB2312");// 设置类型
        response.setHeader("Pragma", "No-cache");// 设置头
        response.setHeader("Cache-Control", "no-cache");// 设置头
        response.setDateHeader("Expires", 0);// 设置日期头

        if(StringUtils.isBlank(version) || EXCEL_FILE_2003.equals(version.trim())){
            exportExcel2003(title, dataset, out, "yyyy-MM-dd hh:mm:ss",true);
        }else{
            exportExcel2007(title, dataset, out, "yyyy-MM-dd hh:mm:ss", true);
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if( out != null){
                try{
                    out.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此版本生成2007以上版本的文件 (文件后缀：xlsx)
     * </p>
     *
     * @param title
     *            表格标题名
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *            JavaBean属性的数据类型有基本数据类型及String,Date
     * @param out
     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param timePattern
     *            如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     * @param firstLine
     *            导出是否带头部标题行
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static<T> void exportExcel2007( String title, Collection<T> dataset, OutputStream out, String timePattern ,boolean firstLine) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet ;
        if (StringUtils.isEmpty(title))
            sheet = workbook.createSheet();
        else
            sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(new XSSFColor(java.awt.Color.gray));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setColor(new XSSFColor(java.awt.Color.BLACK));
        font.setFontHeightInPoints((short) 11);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(new XSSFColor(java.awt.Color.WHITE));
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBold(true);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 遍历集合数据，产生数据行
        T t ;
        Matcher matcher;
        XSSFRow row;
        XSSFCell cell;
        Object value;
        String textValue;
        XSSFRichTextString richString;

        int index = 0;
        boolean flag = false;
        Pattern pattern = Pattern.compile("^//d+(//.//d+)?$");
        SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
        List<Field> fields = new ArrayList<>() ;
        List<ExcelField> usedFields = new ArrayList<>();
        Iterator<T> it = dataset.iterator();

        if (!dataset.isEmpty()) {
            t = (T) it.next();
            Class clazz = t.getClass();
            while (clazz != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                fields.addAll(Arrays.asList(clazz .getDeclaredFields()));
                clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
            }

            for (Field f: fields) {
                if(f.isAnnotationPresent(Excel.class)) {
                    f.setAccessible(true);
                    Excel annotation = f.getAnnotation(Excel.class);
                    if (annotation.isDIYClass()) {
                        Class tempClazz = f.getType();
                        Field[] field2 = tempClazz.getDeclaredFields();
                        for (Field f2: field2
                                ) {
                            if(f2.isAnnotationPresent(Excel.class)) {
                                f2.setAccessible(true);
                                Excel annotation2 =f2.getAnnotation(Excel.class);
                                usedFields.add(new ExcelField(f2,annotation2.index(),annotation2.name(),f));
                            }
                        }
                    }else {
                        usedFields.add(new ExcelField(f, annotation.index(), annotation.name()));
                    }
                }
            }
            Collections.sort(usedFields);

            // 产生表格标题行
            if(firstLine) {
                row = sheet.createRow(index++);
                XSSFCell cellHeader;
                for (int i = 0; i < usedFields.size(); i++) {
                    cellHeader = row.createCell(i);
                    cellHeader.setCellStyle(style);
                    String head = usedFields.get(i).getHead();
                    Field superField = usedFields.get(i).getSuperField();
                    if (StringUtils.isEmpty(head) && superField != null) {
                        cellHeader.setCellValue(new XSSFRichTextString(superField.getAnnotation(Excel.class).name()));
                    }else{
                        cellHeader.setCellValue(new XSSFRichTextString(head));
                    }
                }
            }

            do{
                if( flag) t = (T) it.next();
                flag = true;
                row = sheet.createRow(index++);
                // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
                int j = 0;
                for (ExcelField ef : usedFields) {
                    cell = row.createCell(j++);
                    cell.setCellStyle(style2);

                    try {
                        if( ef.superField == null) {
                            value = ef.getField().get(t);
                        }else{
                            if(ef.superField.get(t) == null)
                                value = null;
                            else
                                value = ef.getField().get(ef.superField.get(t));
                        }
                        // 判断值的类型后进行强制类型转换
                        textValue = null;
                        textValue = getString(cell, value, sdf);
                        if (textValue != null) {
                            matcher = pattern.matcher(textValue);
                            if (matcher.matches()) {
                                // 是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                richString = new XSSFRichTextString(textValue);
                                cell.setCellValue(richString);
                            }
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }while (it.hasNext());
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此方法生成2003版本的excel,文件名后缀：xls <br>
     * </p>
     *
     * @param title
     *            表格标题名
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *            JavaBean属性的数据类型有基本数据类型及String,Date
     * @param out
     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param timePattern
     *            如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     * @param firstLine
     *            导出是否带头部标题行
     *
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static<T> void exportExcel2003(String title,  Collection<T> dataset, OutputStream out, String timePattern,boolean firstLine) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet;
        if (StringUtils.isEmpty(title))
            sheet = workbook.createSheet();
        else
            sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式

        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
//        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font.setBold(true);
        // 把字体应用到当前的样式
        style2.setFont(font2);


        // 遍历集合数据，产生数据行
        T t;
        Matcher matcher;
        HSSFRow row;
        HSSFCell cell;
        Object value;
        String textValue;
        HSSFRichTextString richString;

        int index = 0;
        boolean flag = false;
        List<Field> fields = new ArrayList<>() ;
        List<ExcelField> usedFields = new ArrayList<>();
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
        Iterator<T> it = dataset.iterator();


        if (!dataset.isEmpty()) {
            t = (T) it.next();
            Class clazz = t.getClass();
            while (clazz != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                fields.addAll(Arrays.asList(clazz .getDeclaredFields()));
                clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
            }

            for (Field f : fields) {
                if (f.isAnnotationPresent(Excel.class)) {
                    f.setAccessible(true);
                    Excel annotation = f.getAnnotation(Excel.class);
                    if (annotation.isDIYClass()) {
                        Class tempClazz = f.getType();
                        Field[] field2 = tempClazz.getDeclaredFields();
                        for (Field f2: field2
                                ) {
                            if(f2.isAnnotationPresent(Excel.class)) {
                                f2.setAccessible(true);
                                Excel annotation2 =f2.getAnnotation(Excel.class);
                                usedFields.add(new ExcelField(f2,annotation2.index(),annotation2.name(),f));
                            }
                        }
                    }else {
                        usedFields.add(new ExcelField(f, annotation.index(), annotation.name()));
                    }
                }
            }
            Collections.sort(usedFields);

            // 产生表格标题行
            if(firstLine) {
                row = sheet.createRow(index++);
                HSSFCell cellHeader;
                for (int i = 0; i < usedFields.size(); i++) {
                    cellHeader = row.createCell(i);
                    cellHeader.setCellStyle(style);
                    cellHeader.setCellValue(new HSSFRichTextString(usedFields.get(i).getHead()));
                }
            }
            do {
                if( flag) t = (T) it.next();
                flag = true;
                row = sheet.createRow(index++);
                int j = 0;
                for (ExcelField ef : usedFields) {
                    cell = row.createCell(j++);
                    cell.setCellStyle(style2);
                    try {
                        if( ef.superField == null) {
                            value = ef.getField().get(t);
                        }else{
                            if(ef.superField.get(t) == null)
                                value = null;
                            else
                                value = ef.getField().get(ef.superField.get(t));
                        }
                        // 判断值的类型后进行强制类型转换
                        textValue = null;
                        textValue = getString(cell, value, sdf);
                        if (textValue != null) {
                            matcher = p.matcher(textValue);
                            if (matcher.matches()) {
                                // 是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                richString = new HSSFRichTextString(textValue);
                                cell.setCellValue(richString);
                            }
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }while (it.hasNext());
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getString(Cell cell, Object value, SimpleDateFormat sdf) {
        String textValue = null;
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Float) {
            textValue = String.valueOf((Float) value);
            cell.setCellValue(textValue);
        } else if (value instanceof Double) {
            textValue = String.valueOf((Double) value);
            cell.setCellValue(textValue);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        if (value instanceof Boolean) {
            textValue = "是";
            if (!(Boolean) value) {
                textValue = "否";
            }
        } else if (value instanceof Date) {
            textValue = sdf.format((Date) value);
        } else {
            // 其它数据类型都当作字符串简单处理
            if (value != null) {
                textValue = value.toString();
            }
        }
        return textValue;
    }


    static class ExcelField implements Comparable<ExcelField> {
        private  Field field;
        private int index;
        private String head;
        private Field superField; //父字段

        public ExcelField(Field field, int index, String head ) {
            this.field = field;
            this.index = index;
            this.head = head;
        }

        public ExcelField(Field field, int index, String head, Field superField) {
            this.field = field;
            this.index = index;
            this.head = head;
            this.superField = superField;
        }

        public Field getSuperField() {
            return superField;
        }

        public void setSuperField(Field superField) {
            this.superField = superField;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        @Override
        public int compareTo(ExcelField o) {
            return this.index - o.index;
        }
    }
}


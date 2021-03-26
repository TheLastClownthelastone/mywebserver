package com.pt;


import com.googlecode.aviator.AviatorEvaluator;
import org.apache.poi.hssf.usermodel.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

public class Test8 {

    @Test
    public void test1(){
        Map<String,Object> map = new HashMap<>();
        map.put("a",1);
        map.put("b",1);
        map.put("c",1);

        String str = "c=a+b";
        String[] split = str.split("=");
        Object execute = AviatorEvaluator.execute(split[1], map);
        map.put(split[0],execute);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("d",3);
        map.putAll(map1);
        System.out.println(map);

    }

    @Test
    public void test2(){
        Long result = (Long) AviatorEvaluator.execute("1+2+3");
        System.out.println(result);

    }

    @Test
    public void test3(){
        Map<String,Object> map = new HashMap<>();
        map.put("a","1");
        map.put("b","2");
        String str = "a+b";
        System.out.println(AviatorEvaluator.execute(str, map));
    }


    @Test
    public void test4(){
        Map<String,Object> map = new HashMap<>();
        map.put("a",1);
        map.put("b",2);
        map.put("c",3);

        String str = "b+c-a*2";
        System.out.println(AviatorEvaluator.execute(str, map));
    }

    @Test
    public void test5() throws Exception{
        List<String> c = Arrays.asList("地面电站","10MW");
        String tittle = String.join("\n",c);
        List<String> list = Arrays.asList("1","2","3","4","6","7");
        String filePath = "D:\\江苏精益\\checkCode\\bw-jsjyh\\bwopt\\booway\\bw-jsjyh\\forecast\\forecastPhotovoltaicInfo.xls";
        InputStream inputStream = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);

        HSSFSheet sheet = workbook.getSheet("光伏电站工程参考造价指标");

        HSSFRow row = sheet.getRow(0);
        HSSFCell cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(tittle);

        for (int i = 0; i < list.size(); i++) {
            HSSFRow row1 = sheet.getRow(i+1);
            HSSFCell cell1 = row1.createCell(5);
            cell1.setCellValue(list.get(i));
        }
        String str = "D:\\testforcast\\1.xls";
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        workbook.write(fileOutputStream);
        inputStream.close();
        workbook.close();
        fileOutputStream.close();
    }

    @Test
    public void test6(){
        double a = 480546816L;
        double b = 534097920L;
        System.out.println(a/b);
    }

    @Test
    public void test7(){
        int a = 2;
        double c = 0;
        double b = 3;
        System.out.println(b/a);
    }

    @Test
    public void test8(){
        double a = 0;
        int b = 3;
        int c = 2;

        System.out.println((a+b)/c);
    }
}

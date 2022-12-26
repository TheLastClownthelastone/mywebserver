package com.pt;

import com.pt.sql.SqlSessionFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nate-pt
 * @date 2022/8/22 10:49
 * @Since 1.8
 * @Description
 */
public class Test11 {
    public static void main(String[] args) {
        String fileName = "canlian_cjr.sql";
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
            inputStreamReader = new InputStreamReader(is);
            reader = new BufferedReader(inputStreamReader);
            String str = null;
            int count = 0;
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 37; i++) {
                reader.readLine();
            }
            while ((str = reader.readLine()) != null) {
                if (str.startsWith("INSERT")) {
                    String[] split = str.split("\\),");
                    Collections.addAll(list,split);
                }
            }
            //list.stream().forEach(System.out::println);
           list =  list.stream().map(s -> {
                if (s.contains("INSERT INTO `canlian_cjr` VALUES")) {
                    s = s.replace("INSERT INTO `canlian_cjr` VALUES","");
                }
                if (!s.endsWith(")")&& !s.endsWith(";")) {
                    s= s+")";
                }
                return s;
            }).collect(Collectors.toList());
            System.out.println(list.size());
            int i = 0;
            Connection connection = SqlSessionFactory.getConnection();
            Statement statement = connection.createStatement();
            for (String s : list) {
                boolean execute = statement.execute("INSERT INTO `canlian_cjr` VALUES" + s);
                if (!execute) {
                    System.out.println("执行失败.......");
                }
                i++;
                if (i == 5000) {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is !=null) {
                    is.close();
                }
                if (inputStreamReader !=null) {
                    inputStreamReader.close();
                }
                if (reader!=null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

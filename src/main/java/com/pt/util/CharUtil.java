package com.pt.util;
public class CharUtil
{

    /**
     * 首字母转小写
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s){
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    /**
     * 去除字符串中的斜杠
     * @param str
     * @return
     */
    public static String removeSlash(String str){
        return str.replace("/","");
    }

}



package com.mobeiwsq.engine_project.utils;
/**
* String工具类
*
* @author : mobeiwsq
* @since :  2025/4/21 15:15
*/

public class StringUtils {
    public static boolean isSpace(String s) {
        if (s == null) {
            return true;
        } else {
            int i = 0;

            for(int len = s.length(); i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}

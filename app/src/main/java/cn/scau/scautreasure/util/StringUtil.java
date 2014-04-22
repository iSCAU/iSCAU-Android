package cn.scau.scautreasure.util;

/**
 * User: special
 * Date: 13-10-13
 * Time: 上午10:27
 * Mail: specialcyci@gmail.com
 */
public class StringUtil {
    public static int findStrTime(String str,String substr){
        int start = 0;
        int count = 0;
        while((start=str.indexOf(substr,start))>0) count++;
        return count;
    }

    public static boolean isEmpty(String str){
        return str == null || str.trim().equals("");
    }
}

package cn.scau.scautreasure.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: special
 * Date: 13-10-13
 * Time: 上午10:27
 * Mail: specialcyci@gmail.com
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        return str == null || str.trim().equals("");
    }

    /***
     * Join a collection of strings by a seperator
     * @param strings collection of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Collection<String> strings, String sep) {
        return join(strings.iterator(), sep);
    }

    /***
     * Join a collection of strings by a seperator
     * @param strings iterator of string objects
     * @param sep string to place between strings
     * @return joined string
     */
    public static String join(Iterator<String> strings, String sep) {
        if (!strings.hasNext())
            return "";

        String start = strings.next();
        if (!strings.hasNext()) // only one, avoid builder
            return start;

        StringBuilder sb = new StringBuilder(64).append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return sb.toString();
    }
}

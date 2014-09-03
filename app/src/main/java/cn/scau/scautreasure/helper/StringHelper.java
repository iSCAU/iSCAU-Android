package cn.scau.scautreasure.helper;

import org.androidannotations.annotations.EBean;

/**
 * User: special
 * Date: 13-9-1
 * Time: 下午1:23
 * Mail: specialcyci@gmail.com
 */
@EBean
public class StringHelper {

    public String join(String join, String[] strAry) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }

}

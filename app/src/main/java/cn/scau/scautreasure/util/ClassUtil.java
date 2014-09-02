package cn.scau.scautreasure.util;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import cn.scau.scautreasure.helper.ClassHelper_;
import cn.scau.scautreasure.model.ClassModel;

/**
 * Created by robust on 14-4-9.
 */
public class ClassUtil {
    /**
     * 上课-小时
     */
    private static final int[] HOURS_ON = {8, 8, 10, 10, 12, 13, 14, 15, 16, 17, 19, 20, 21};
    /**
     * 上课-分钟
     */
    private static final int[] MINUTES_ON = {0, 50, 5, 55, 30, 20, 30, 20, 35, 25, 30, 20, 10};
    /**
     * 下课-小时
     */
    private static final int[] HOURS_OVER = {8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 20, 21, 21};
    /**
     * 下课-分钟
     */
    private static final int[] MINUTES_OVER = {45, 35, 50, 40, 15, 5, 15, 5, 20, 10, 15, 5, 55};

    /**
     * 上课时间
     *
     * @param c    时间，设置好小时和分钟
     * @param node 节次
     */
    public static String genClassBeginTime(Calendar c, int node) {
        c.set(Calendar.HOUR_OF_DAY, HOURS_ON[node - 1]);
        c.set(Calendar.MINUTE, MINUTES_ON[node - 1]);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return  String.format("%2d:%2d",HOURS_ON[node-1],MINUTES_ON[node-1]); //不足两位，补零
    }

    /**
     * 下课时间
     *
     * @param c    时间，设置好小时和分钟
     * @param node 节次
     */
    public static void genClassOverTime(Calendar c, int node) {
        c.set(Calendar.HOUR_OF_DAY, HOURS_OVER[node - 1]);
        c.set(Calendar.MINUTE, MINUTES_OVER[node - 1]);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);


    }

    /**
     * 判断当前是否处于上课期间，使用二分查找
     *
     * @param context
     * @return
     */
    public static boolean isDuringClassNow(Context context) {
        long cur = System.currentTimeMillis();
        long lt, ht;
        ClassHelper_ helper = ClassHelper_.getInstance_(context);
        List<ClassModel> klasses = helper.getDayLessonWithParams(new DateUtil().getDayOfWeek());
        int low = 0;
        int high = klasses.size() - 1;
        int mid;
        Calendar c = Calendar.getInstance();
        String[] nodes;
        while (low <= high) {
            mid = (high + low) / 2;
            nodes = klasses.get(mid).getNode().split(",");
            genClassBeginTime(c, Integer.parseInt(nodes[0]));
            lt = c.getTimeInMillis();
            genClassOverTime(c, Integer.parseInt(nodes[nodes.length - 1]));
            ht = c.getTimeInMillis();
            if (lt <= cur && ht >= cur) {
                return true;
            } else if (lt > cur) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return false;
    }
}

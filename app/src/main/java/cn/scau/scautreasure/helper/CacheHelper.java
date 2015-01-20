package cn.scau.scautreasure.helper;

import android.content.Context;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.util.CacheUtil;

/**
 * 封装在 上下文中 使用的缓存操作函数。
 * <p/>
 * Created by special on 14-6-2.
 */
@EBean
public class CacheHelper {

    private CacheUtil cacheUtil;
    private Context mContext;
    private String cacheKey;

    public CacheHelper(Context context) {
        this.mContext = context;
        cacheUtil = CacheUtil.get(this.mContext);
    }

    /**
     * 设置当前窗口的缓存键，自动加上当前用户的用户名，
     * 以区分。
     *
     * @param cacheKey
     */
    public void setCacheKey(String cacheKey) {
        this.cacheKey = AppContext.userName + "_" + cacheKey;
    }

    /**
     * 从硬盘加载缓存， 并赋值到 List 中。
     */
    public ArrayList loadListFromCache() {
        return (ArrayList) cacheUtil.getAsObject(cacheKey);
    }

    /**
     * 将 List 中的数据固化写入到硬盘当中。
     */
    public void writeListToCache(ArrayList list) {
        cacheUtil.put(cacheKey, list);
    }
}

package cn.scau.scautreasure.helper;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.scau.scautreasure.util.CacheUtil;

/**
 * 缓存操作函数
 * Created by macroyep on 4/11/15.
 */
@EBean
public class WebViewCacheHelper {
    private CacheUtil cacheUtil;
    private Context context;

    public WebViewCacheHelper(Context context) {
        this.context = context;
        cacheUtil = CacheUtil.get(this.context);
    }


    /**
     * 读取list缓存
     *
     * @param cacheKey
     * @return
     */
    public ArrayList readListFromCache(String cacheKey) {
        return (ArrayList) cacheUtil.getAsObject(cacheKey);
    }

    /**
     * 写list到缓存
     *
     * @param cacheKey
     * @param list
     */
    public void writeListToCache(String cacheKey, ArrayList list) {
        cacheUtil.put(cacheKey, list);
    }

    /**
     * 读取json结构的数据
     *
     * @param cacheKey
     * @return
     */
    public JSONObject readJSONFromCache(String cacheKey) {
        return cacheUtil.getAsJSONObject(cacheKey);
    }

    /**
     * 写json结构数据到缓存
     *
     * @param cacheKey
     * @param jsonObject
     */
    public void writeJSONToCache(String cacheKey, JSONObject jsonObject) {
        cacheUtil.put(cacheKey, jsonObject);
    }


    /**
     * 读取字符串
     *
     * @param cacheKey
     * @return
     */
    public String readStringFromCache(String cacheKey) {
        String cache = cacheUtil.getAsString(cacheKey);
        if (cache == null)
            return "";
        return cache;
    }

    /**
     * 写字符串
     *
     * @param cacheKey
     * @param string
     */
    public void writeStringToCache(String cacheKey, String string) {
        cacheUtil.put(cacheKey, string);
    }

    /**
     * 写字符串
     *
     * @param cacheKey
     * @param string
     * @param time 秒
     */
    public void writeStringToCache(String cacheKey, String string, int time) {
        cacheUtil.put(cacheKey, string, time);
    }


}

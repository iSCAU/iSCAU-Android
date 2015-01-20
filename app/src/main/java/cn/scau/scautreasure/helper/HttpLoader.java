package cn.scau.scautreasure.helper;

import android.util.Log;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.api.SchoolActivityApi;
import cn.scau.scautreasure.model.ActivityCountModel;
import cn.scau.scautreasure.model.ClassModel;

/**
 * Created by macroyep on 15/1/17.
 * Time:14:15
 */
@EBean
public class HttpLoader {
    @App
    AppContext app;

    //校园活动api
    @RestService
    SchoolActivityApi schoolActivityApi;

    //教务处api
    @RestService
    EdusysApi edusysApi;



    @Bean
    ClassHelper classHelper;

    /**
     * 更新课表,写入数据库
     *
     * @param callBack
     */
    @Background
    public void updateClassTable(NormalCallBack callBack) {
        ClassModel.ClassList l = null;
        try {
            l = edusysApi.getClassTable(AppContext.userName, app.getEncodeEduSysPassword());
            app.config.termStartDate().put(l.getTermStartDate());
            // save data in sqlite;
            classHelper.replaceAllLesson(l.getClasses());

        } catch (HttpStatusCodeException e) {
            //服务端请求返回错误
            callBack.onError(e.getStatusCode().value());
            LogCenter.e(getClass(), Thread.currentThread(), "更新课表请求服务端失败");
        } catch (Exception e) {
            //网络错误
            callBack.onNetworkError(null);
            LogCenter.e(getClass(), Thread.currentThread(), "网络错误");

        }
        if (l != null) {
            LogCenter.i(getClass(), Thread.currentThread(), "下载课表成功");

            callBack.onSuccess(null);
        }
    }

    /**
     * 更新活动圈标记
     *
     * @param callBack
     */
    @Background(delay = 500)
    public void updateActivityFlags(NormalCallBack callBack) {
        ActivityCountModel model = null;
        try {
            model = schoolActivityApi.getActivityCount(String.valueOf(app.config.lastRedPoint().get()));
            if (Integer.parseInt(model.getResult()) > 0) {
                LogCenter.i(getClass(), Thread.currentThread(), "校园活动有更新");
                callBack.onSuccess("yes");
            } else {
                callBack.onSuccess("no");
            }
        } catch (HttpStatusCodeException e) {
            LogCenter.i(getClass(), Thread.currentThread(), "活动圈是否有更新标记失败");
            callBack.onError(null);
        } catch (Exception e) {
            LogCenter.e(getClass(), Thread.currentThread(), "网络错误");
            callBack.onNetworkError(null);
        }
    }


    public interface NormalCallBack {
        public void onSuccess(Object obj);

        public void onError(Object obj);

        public void onNetworkError(Object obj);

    }
}

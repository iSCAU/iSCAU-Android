package cn.scau.scautreasure.helper;

import android.util.Log;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.api.BusApi;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.api.FunctionApi;
import cn.scau.scautreasure.api.SchoolActivityApi;
import cn.scau.scautreasure.model.ActivityCountModel;
import cn.scau.scautreasure.model.BusLineModel;
import cn.scau.scautreasure.model.BusSiteModel;
import cn.scau.scautreasure.model.BusStateModel;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.FunctionModel;

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


    //bus api
    @RestService
    BusApi busApi;

    //功能api
    @RestService
    FunctionApi functionApi;


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
                LogCenter.i(getClass(), Thread.currentThread(), "校园活动有更新:" + model.getResult());
                callBack.onSuccess(Integer.parseInt(model.getResult()));
            } else {
                callBack.onSuccess(0);
            }
        } catch (HttpStatusCodeException e) {
            LogCenter.i(getClass(), Thread.currentThread(), "活动圈是否有更新标记失败");
            callBack.onError(null);
        } catch (Exception e) {
            LogCenter.e(getClass(), Thread.currentThread(), "网络错误");
            callBack.onNetworkError(null);
        }
    }

    /**
     * 更新校巴站点数据
     *
     * @param callBack
     */
    @Background
    public void updateBusStation(String line, String direction, NormalCallBack callBack) {
        ArrayList<BusSiteModel> siteList = null;
        try {
            siteList = busApi.getSite(line, direction).getSites();
        } catch (HttpStatusCodeException e) {
            callBack.onError(e.getStatusCode().value());
        } catch (Exception e) {
            callBack.onNetworkError(null);
        }
        if (siteList != null) {
            callBack.onSuccess(siteList);
        }

    }

    /**
     * 更新校巴位置数据
     *
     * @param callBack
     */
    @Background
    public void updateBusPos(NormalCallBack callBack, Object... params) {
        List<BusStateModel> stateList = null;
        try {
            stateList = busApi.getBusState((String) params[0], (String) params[1]).getStates();
        } catch (HttpStatusCodeException e) {
            callBack.onError(e.getStatusCode().value());
        } catch (Exception e) {
            callBack.onNetworkError(null);
        }
        if (stateList != null) {
            callBack.onSuccess(stateList);
        }
    }

    /**
     * 加载校巴线路数据
     *
     * @param callBack
     */
    public void updateBusLine(NormalCallBack callBack) {
        ArrayList<BusLineModel> lineList = null;
        try {
            lineList = busApi.getLine().getLines();
        } catch (HttpStatusCodeException e) {
            callBack.onError(e.getStatusCode().value());
        } catch (Exception e) {
            callBack.onNetworkError(null);
        }
        if (lineList != null) {
            callBack.onSuccess(lineList);
        }
    }

    /**
     * 加载动态应用列表
     *
     * @param callBack
     */
    @Background
    public void getFunctionList(NormalCallBack callBack) {
        FunctionModel.FunctionList list = null;
        try {
            list = functionApi.getFunctionList(app.userName, app.deviceToken);
        } catch (HttpStatusCodeException e) {
            callBack.onError(null);
        } catch (Exception e) {
            callBack.onNetworkError(null);
        }
        if (list != null) {
            callBack.onSuccess(list);
        }

    }

    /**
     * 常规回调
     */
    public interface NormalCallBack {
        public void onSuccess(Object obj);

        public void onError(Object obj);

        public void onNetworkError(Object obj);

    }
}

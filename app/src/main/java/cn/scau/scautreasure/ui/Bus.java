package cn.scau.scautreasure.ui;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.BusApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BusLineModel;
import cn.scau.scautreasure.model.BusSiteModel;
import cn.scau.scautreasure.model.BusStateModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.BusWidget;
import cn.scau.scautreasure.widget.ParamWidget;
import cn.scau.scautreasure.widget.ParamWidget_;

/**
 * User: special
 * Date: 13-8-23
 * Time: 下午4:40
 * Mail: specialcyci@gmail.com
 */
@EFragment  (R.layout.bus)
@OptionsMenu(R.menu.menu_bus)
public class Bus extends Common{

    @RestService BusApi api;
    @App         AppContext app;
    @ViewById    BusWidget busWidget;
    @ViewById    LinearLayout layout_parent;
    @StringArrayRes
    String[] both_direction,both_direction_eng;
    @StringArrayRes
    String[] cycle_direction,cycle_direction_eng;
    private ArrayList<BusLineModel> lineList;
    private ArrayList<BusSiteModel>  siteList;
    private List<BusStateModel> stateList;
    private CacheUtil cacheUtil;
    private boolean isAutomateRefresh = false;
    ParamWidget wheel_line,wheel_direction;

    @AfterViews
    void init(){
        setTitle(R.string.title_bus);
        setDataEmptyTips(R.string.tips_bus_loading_err);
        cacheUtil  = CacheUtil.get(getSherlockActivity());
        UIHelper.getDialog(R.string.loading_bus_route).show();
        loadLine();
    }

    /**
     * when user
     */
    @Override
    public void onDestroyView() {
        BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG,true);
        handler.removeCallbacks(runnable);
        super.onDestroyView();
    }

    /**
     * main button of refresh
     */
    @OptionsItem
    void menu_refresh(){
        refreshSiteAndBus();
    }

    /**
     * main button of automate refresh;
     */
    @OptionsItem
    void menu_automate_refresh(MenuItem item){
        isAutomateRefresh = !isAutomateRefresh;

        if(isAutomateRefresh){
            item.setTitle(R.string.menu_automate_refresh_close);
            handler.postDelayed(runnable,1000);
        }else{
            item.setTitle(R.string.menu_automate_refresh);
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * the timer to automate load data
     */
    private Handler handler = new Handler( );
    private Runnable runnable = new Runnable( ) {
        public void run ( ) {
            refreshSiteAndBus();
            handler.postDelayed(this,30 * 1000);
        }
    };

    /**
     * listen to the wheels change
     */
    private OnWheelChangedListener wheelChange = new OnWheelChangedListener() {
        @Override
        public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {

            BusLineModel _line  = lineList.get(wheel_line.getWheel().getCurrentItem());

            if(wheel ==  wheel_line.getWheel()){
                // 切换线路的话，重新装载方向;
                String[] direction = getDirectionByLine(_line);
                wheel_direction.initView(getString(R.string.listitem_lable_direction),direction,1);

            }else{
                // 切换方向的话，加载新的站点信息;
                refreshSiteAndBus();
            }

        }
    };

    /**
     * return the string array of direction which to display in UI;
     * @param line
     * @return
     */
    private String[] getDirectionByLine(BusLineModel line){
        String lineType = line.getLineType();

        if (lineType.equals("BOTH_DIRECT")){
            return both_direction;
        }else if (lineType.equals("CYCLE_DIRECT")){
            return cycle_direction;
        }
        return null;
    }


    private String[] getDirectionEngByLine(BusLineModel line){
        String lineType = line.getLineType();

        if (lineType.equals("BOTH_DIRECT")){
            return both_direction_eng;
        }else if (lineType.equals("CYCLE_DIRECT")){
            return cycle_direction_eng;
        }
        return null;
    }

    //--------------------------------------------------------------------------
    //
    //  UI Related;
    //
    //--------------------------------------------------------------------------

    @UiThread(delay = 300)
    void showLine(){
        UIHelper.getDialog().dismiss();

        // build line information;
        int i = 0;
        String[] line = new String[lineList.size()];
        for (BusLineModel r : lineList)  line[i++] = r.getLineNum();

        // build up wheel control;
        buildWheel(line);

        // add to view;
        layout_parent.addView(wheel_line,0);
        layout_parent.addView(wheel_direction,1);

        addIgnoredViewList();
    }


    private void addIgnoredViewList(){
        addIgnoredView(wheel_line);
        addIgnoredView(wheel_direction);
    }


    @UiThread
    void showSiteAndBus(){
        if(siteList != null)
            busWidget.initView(siteList, stateList);
    }

    /**
     * call relate method to refresh the site and bus
     */
    @UiThread
    void refreshSiteAndBus(){

        AppMsg.makeText(getSherlockActivity(),R.string.tips_bus_loading, AppMsg.STYLE_INFO).show();

        BusLineModel     _line = lineList.get(wheel_line.getWheel().getCurrentItem());
        String[] direction_eng = getDirectionEngByLine(_line);
        String            line = wheel_line.getSelectedParam();
        String       direction = direction_eng[wheel_direction.getWheel().getCurrentItem()];

        loadSite(line, direction);
        loadData(line, direction);
    }

    /**
     * help to build line and direction wheel;
     * @param line
     */
    private void buildWheel(String[] line){
        wheel_line      = ParamWidget_.build(getSherlockActivity());
        wheel_direction = ParamWidget_.build(getSherlockActivity());
        wheel_line.getWheel().addChangingListener(wheelChange);
        wheel_direction.getWheel().addChangingListener(wheelChange);

        wheel_line.initView(getString(R.string.listitem_lable_line),line,0);
        wheel_direction.initView(getString(R.string.listitem_lable_direction),getDirectionByLine(lineList.get(0)),1);
    }

    //--------------------------------------------------------------------------
    //
    //  Network Request;
    //
    //--------------------------------------------------------------------------

    /**
     * loading line informations
     */
    @Background( id = UIHelper.CANCEL_FLAG )
    void loadLine(){
        loadCacheLineList();
        if (lineList == null){
            try{
                lineList = api.getLine().getLines();
                saveCacheLineList();
                showLine();
            }catch (HttpStatusCodeException e){
                showErrorResult(getSherlockActivity(), e.getStatusCode().value());
            }
        }
    }

    /**
     * loading the bus stop of the route;
     * @param line
     * @param direction
     */
    @Background( id = UIHelper.CANCEL_FLAG )
    void loadSite(String line, String direction) {
        loadCacheSiteList(line,direction);

        if (siteList == null){
            try{
                siteList = api.getSite(line,direction).getSites();
                saveCacheSiteList(line,direction);
            }catch (HttpStatusCodeException e){
                showErrorResult(getSherlockActivity(), e.getStatusCode().value());
            }
        }

    }

    /**
     * load the bus location information;
     * @param params
     */
    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            stateList = api.getBusState((String) params[0], (String) params[1]).getStates();
            showSiteAndBus();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }
    }

    private void loadCacheLineList(){
        lineList = (ArrayList<BusLineModel>) cacheUtil.getAsObject("bus_line");
    }

    private void saveCacheLineList(){
        cacheUtil.put("bus_line", lineList , AppConstant.BUS_LINE_CACHE_TIME);
    }

    private void loadCacheSiteList(String line,String direction){
        String key = "bus_site_" + line + direction;
        siteList   = (ArrayList<BusSiteModel>) cacheUtil.getAsObject(key);
    }

    private void saveCacheSiteList(String line,String direction){
        cacheUtil.put("bus_site_" + line + direction, siteList, AppConstant.BUS_SITE_CACHE_TIME);
    }

}
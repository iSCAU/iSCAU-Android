package cn.scau.scautreasure.ui;

import android.os.Handler;
import android.util.Log;


import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;


import com.gc.materialdesign.widgets.Dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.rest.RestService;

import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;


import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.BusApi;

import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BusLineModel;
import cn.scau.scautreasure.model.BusSiteModel;
import cn.scau.scautreasure.model.BusStateModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.BusTabWidget;

import cn.scau.scautreasure.widget.BusWidget;


import cn.scau.scautreasure.widget.RefreshIcon;


@EFragment(R.layout.bus)
public class FragmentBus extends BaseFragment {
    private String direct = "up";//up:西园>荷园;down:荷园>西园

    //线路id
    private int line_id = 1;//1,2

    @ViewById(R.id.more)
    RefreshIcon refreshButton;
    @ViewById(R.id.title_text)
    TextView title_text;


    @ViewById(R.id.titles)
    BusTabWidget titles;

    @RestService
    BusApi api;
    @App
    AppContext app;
    @ViewById
    BusWidget busWidget;
    @ViewById
    LinearLayout layout_parent;
    @StringArrayRes
    String[] both_direction, both_direction_eng;
    @StringArrayRes
    String[] cycle_direction, cycle_direction_eng;

    private ArrayList<BusLineModel> lineList;
    private ArrayList<BusSiteModel> siteList;
    private List<BusStateModel> stateList;
    private CacheUtil cacheUtil;
    private boolean isAutomateRefresh = false;
    private RefreshIcon refreshIcon;


    /**
     * 切换路线
     */
    @Click(R.id.title)
    void changeLine() {
        final Dialog dialog = new Dialog(getActivity(), "选择校巴线路", "1号线:荷园<->西园\n2号线:荷园<->车队候车场(环行)");
        // Set accept click listenner
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.i(getClass().getName(), "选择了2号线路");
                title_text.setText("2号线(环行)");
                if (line_id == 1) {
                    String[] direction = getDirectionByLine(lineList.get(1));

                    titles.setVisibility(View.GONE);
                }
                refreshSiteAndBus();

                line_id = 2;
                app.config.default_bus_line().put(line_id);
//
            }
        });
        // Set cancel click listenner

        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.i(getClass().getName(), "选择了1号线路");
                title_text.setText("1号线");
                if (line_id == 2) {
                    titles.setVisibility(View.VISIBLE);
                }
                refreshSiteAndBus();
                line_id = 1;
                app.config.default_bus_line().put(line_id);

            }
        });


        dialog.show();
        dialog.getButtonCancel().setText("1号线");
        dialog.getButtonAccept().setText("2号线");
        Log.i(getClass().getName(), dialog.getButtonAccept() == null ? "null" : "nonull");

    }

    /**
     * the timer to automate load data
     */
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            refreshSiteAndBus();
            handler.postDelayed(this, 30 * 1000);
        }
    };

    @AfterViews
    void init() {
        line_id = app.config.default_bus_line().get();
        if (line_id == 2) {
            titles.setVisibility(View.GONE);
            title_text.setText("2号线(环行)");

        } else {
            title_text.setText("1号线");
        }
        showTab();
        cacheUtil = CacheUtil.get(getActivity());
        loadLine();

    }

    /**
     * 标签的点击,切换线路方向；
     */
    private BusTabWidget.onTabChangeListener onTabChangeListener = new BusTabWidget.onTabChangeListener() {
        @Override
        public void change(int posistion) {
            if (posistion == 0) {
                direct = "up";
            } else if (posistion == 1) {
                direct = "down";
            }
            refreshSiteAndBus();
        }
    };

    /*
     * 这里因为种种原因要这么做，getWidth=0是没办法避免的事情，只好等待
     * 另外在titles成功changeTab到 <今天> 这个标签栏之后，再设置相应的listener。
     */
    @UiThread(delay = 30)
    void showTab() {
        if (titles.getWidth() == 0) showTab();
        else {
            titles.changeTab(0);
            titles.setListener(onTabChangeListener);
        }
    }

    /**
     * when user
     */
    @Override
    public void onDestroyView() {
        handler.removeCallbacks(runnable);
        super.onDestroyView();
    }


    /**
     * return the string array of direction which to display in UI;
     *
     * @param line
     *
     * @return
     */
    private String[] getDirectionByLine(BusLineModel line) {
        String lineType = line.getLineType();

        if (lineType.equals("BOTH_DIRECT")) {
            return both_direction;
        } else if (lineType.equals("CYCLE_DIRECT")) {
            return cycle_direction;
        }
        return null;
    }


    private String[] getDirectionEngByLine(BusLineModel line) {
        String lineType = line.getLineType();

        if (lineType.equals("BOTH_DIRECT")) {
            return both_direction_eng;
        } else if (lineType.equals("CYCLE_DIRECT")) {
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
    void showLine() {
        refreshButton.stopProgress();
        // build line information;
        int i = 0;
        String[] line = new String[lineList.size()];
        for (BusLineModel r : lineList) {
            line[i++] = r.getLineNum();
        }


    }


    @UiThread
    void showSiteAndBus() {
        refreshButton.stopProgress();

        if (siteList != null)
            busWidget.initView(siteList, stateList);
    }

    /**
     * call relate method to refresh the site and bus
     */
    @UiThread
    void refreshSiteAndBus() {
        refreshButton.startProgress();
        if (lineList == null) {
            loadLine();
        } else {
            String line = String.valueOf(line_id);
            if (line_id == 1) {
                loadSite(line, direct);
                loadData(line, direct);
            } else if (line_id == 2) {
                loadSite(line, "circle");
                loadData(line, "circle");
            }

        }
    }

    //--------------------------------------------------------------------------
    //
    //  Network Request;
    //
    //--------------------------------------------------------------------------

    /**
     * loading line informations
     */
    @Background(id = UIHelper.CANCEL_FLAG)
    void loadLine() {
        loadCacheLineList();
        if (lineList == null) {
            try {
                Log.d("-----bus----", "pre");
                lineList = api.getLine().getLines();
                Log.d("-----bus----", "get");
                saveCacheLineList();
                showLine();
                refreshSiteAndBus();

            } catch (HttpStatusCodeException e) {
                showError(e.getStatusCode().value());
                error();
            }
        } else {
            Log.d("-----bus----", "show_line");
            showLine();
            refreshSiteAndBus();
        }
    }

    @UiThread
    void error() {
        refreshButton.stopProgress();
    }

    /**
     * loading the bus stop of the route;
     *
     * @param line
     * @param direction
     */
    @Background(id = UIHelper.CANCEL_FLAG)
    void loadSite(String line, String direction) {
        loadCacheSiteList(line, direction);

        if (siteList == null) {
            try {
                siteList = api.getSite(line, direction).getSites();
                saveCacheSiteList(line, direction);
            } catch (HttpStatusCodeException e) {
//                showErrorResult(getActivity(), e.getStatusCode().value());
            }
        }

    }

    /**
     * load the bus location information;
     *
     * @param params
     */
    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        try {
            stateList = api.getBusState((String) params[0], (String) params[1]).getStates();
            showSiteAndBus();
        } catch (HttpStatusCodeException e) {
            showError(e.getStatusCode().value());

        }
    }

    private void loadCacheLineList() {
        lineList = (ArrayList<BusLineModel>) cacheUtil.getAsObject("bus_line");
    }

    private void saveCacheLineList() {
        cacheUtil.put("bus_line", lineList, AppConstant.BUS_LINE_CACHE_TIME);
    }

    private void loadCacheSiteList(String line, String direction) {
        String key = "bus_site_" + line + direction;
        siteList = (ArrayList<BusSiteModel>) cacheUtil.getAsObject(key);
    }

    private void saveCacheSiteList(String line, String direction) {
        cacheUtil.put("bus_site_" + line + direction, siteList, AppConstant.BUS_SITE_CACHE_TIME);
    }


    @Click(R.id.more)
    void refresh() {
        refreshSiteAndBus();

    }
}
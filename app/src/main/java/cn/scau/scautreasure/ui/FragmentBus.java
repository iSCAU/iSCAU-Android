package cn.scau.scautreasure.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.gc.materialdesign.widgets.Dialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;

import org.androidannotations.annotations.EFragment;

import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.rest.RestService;

import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;


import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.BusApi;

import cn.scau.scautreasure.helper.HttpLoader;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BusLineModel;
import cn.scau.scautreasure.model.BusSiteModel;
import cn.scau.scautreasure.model.BusStateModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.BusTabWidget;

import cn.scau.scautreasure.widget.BusWidget;


import cn.scau.scautreasure.widget.RefreshActionItem;
import cn.scau.scautreasure.widget.RefreshIcon;


@EFragment(R.layout.bus)
@OptionsMenu(R.menu.menu_bus)
public class FragmentBus extends BaseFragment implements RefreshActionItem.RefreshButtonListener {

    @OptionsMenuItem(R.id.refresh_button)
    MenuItem refresh_button;

    protected RefreshActionItem mRefreshActionItem;

    private String direct = "up";//up:西园>荷园;down:荷园>西园

    //线路id
    private int line_id = 1;//1,2


    @ViewById(R.id.titles)
    BusTabWidget titles;


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


    @AfterViews
    void initViews() {
        if (!isAfterViews) {
            isAfterViews = true;
            System.out.println("校巴");
            setCurrentLine();
            cacheUtil = CacheUtil.get(getActivity());
            refreshSiteAndBus();
        }

    }


    /**
     * 加载线路站点
     *
     * @param line
     * @param direction
     */
    @Background
    void loadSite(final String line, final String direction) {
        loadCacheSiteList(line, direction);
        if (siteList == null) {
            httpLoader.updateBusStation(line, direction, new HttpLoader.NormalCallBack() {
                @Override
                public void onSuccess(Object obj) {
                    siteList = (ArrayList<BusSiteModel>) obj;
                    saveCacheSiteList(line, direction);

                }

                @Override
                public void onError(Object obj) {
                    showError(Integer.parseInt(String.valueOf(obj)));
                    error();
                }

                @Override
                public void onNetworkError(Object obj) {
                    showNetWorkError(obj);
                    error();
                }
            });
        }
    }

    /**
     * 加载校巴位置数据
     *
     * @param params
     */
    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        httpLoader.updateBusPos(new HttpLoader.NormalCallBack() {
            @Override
            public void onSuccess(Object obj) {
                stateList = (List<BusStateModel>) obj;
                showSiteAndBus();
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
            }

            @Override
            public void onError(Object obj) {
                showError(Integer.parseInt(String.valueOf(obj)));
                error();
            }

            @Override
            public void onNetworkError(Object obj) {
                showNetWorkError(obj);
                error();
            }
        }, (String) params[0], (String) params[1]);
    }

    /**
     * 加载线路
     */
    @Background
    void loadLine() {
        loadCacheLineList();
        if (lineList == null) {
            httpLoader.updateBusLine(new HttpLoader.NormalCallBack() {
                @Override
                public void onSuccess(Object obj) {
                    lineList = (ArrayList<BusLineModel>) obj;
                    saveCacheLineList();
                    String line = String.valueOf(line_id);
                    if (line_id == 1) {
                        loadSite(line, direct);
                        loadData(line, direct);
                    } else if (line_id == 2) {
                        loadSite(line, "circle");
                        loadData(line, "circle");
                    }
                    System.out.println("从网络加载线路");
                }

                @Override
                public void onError(Object obj) {
                    showError(Integer.parseInt(String.valueOf(obj)));
                }

                @Override
                public void onNetworkError(Object obj) {
                    error();
                }
            });


        } else {
            System.out.println("从缓存加载线路");

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

    /**
     * 设置默认路线
     */
    void setCurrentLine() {
        line_id = app.config.default_bus_line().get();
        if (line_id == 2) {
            titles.setVisibility(View.GONE);
            getTheActionBar().setTitle("2号线(环行)");

        } else {
            getTheActionBar().setTitle("1号线");
        }
        showTab();


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

    /**
     * 更新ui,显示线路
     */
    @UiThread(delay = 300)
    void showLine() {
        int i = 0;
        String[] line = new String[lineList.size()];
        for (BusLineModel r : lineList) {
            line[i++] = r.getLineNum();
        }


    }


    @UiThread
    void showSiteAndBus() {
        mRefreshActionItem.stopProgress();
        Log.i("刷新校巴", String.valueOf(siteList.size()) + ":" + String.valueOf(stateList.size()));
        showLine();

        if (siteList != null) {
            if (stateList.size() == 0) {
                AppToast.show(getActivity(), "现在路上没有校巴...", 0);
            }
            busWidget.initView(siteList, stateList);

        }

    }

    /**
     * call relate method to refresh the site and bus
     */
    @UiThread
    void refreshSiteAndBus() {
        System.out.println("刷新一次");
        mRefreshActionItem.startProgress();
        loadLine();

    }


    @UiThread
    void error() {
        mRefreshActionItem.stopProgress();
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

    /**
     * 刷新校巴状态
     *
     * @param refreshActionItem
     */
    @Override
    public void onRefresh(RefreshActionItem refreshActionItem) {
        refreshSiteAndBus();
    }

    @OptionsItem(R.id.menu_bus_line)
    void menu_bus_line() {

        final Dialog dialog = new Dialog(getActivity(), "选择校巴线路", "1号线:荷园<->西园\n2号线:荷园<->车队候车场(环行)");
        // Set accept click listenner
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.i(getClass().getName(), "选择了2号线路");
                getTheActionBar().setTitle("2号线(环行)");
                if (line_id == 1) {
                    String[] direction = getDirectionByLine(lineList.get(1));

                    titles.setVisibility(View.GONE);
                }
                refreshSiteAndBus();

                line_id = 2;
                app.config.default_bus_line().put(line_id);

            }
        });
        // Set cancel click listenner

        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.i(getClass().getName(), "选择了1号线路");
                getTheActionBar().setTitle("1号线");
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


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mRefreshActionItem = (RefreshActionItem) MenuItemCompat.getActionView(refresh_button);
        mRefreshActionItem.setMenuItem(refresh_button);
        mRefreshActionItem.setRefreshButtonListener(this);
    }

}
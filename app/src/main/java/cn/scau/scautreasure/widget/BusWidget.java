package cn.scau.scautreasure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

import org.androidannotations.annotations.EViewGroup;

import java.util.List;

import cn.scau.scautreasure.model.BusSiteModel;
import cn.scau.scautreasure.model.BusStateModel;

/**
 * 校巴报站的控件
 * <p/>
 * User: special
 * Date: 13-8-23
 * Time: 下午7:53
 * Mail: specialcyci@gmail.com
 */
@EViewGroup
public class BusWidget extends TableLayout {

    Context ctx;

    public BusWidget(Context context) {
        super(context);
        init(context);

    }

    public BusWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.ctx = context;
    }

    /**
     * set up data and view;
     *
     * @param siteList
     * @param stateList
     */
    public void initView(List<BusSiteModel> siteList, List<BusStateModel> stateList) {
        removeAllViews();

        for (BusSiteModel site : siteList) {

            BusStationWidget stationWidget = BusStationWidget_.build(ctx);
            stationWidget.setStationAndIndex(site.getSite(), site.getId());
            addView(stationWidget);

            if (stateList != null)
                for (BusStateModel state : stateList) {
                    // if the bus near the stop, add view after the station;
                    if (isMatchStop(site, state) && isMatchDirection(site, state)) {

                        BusStateWidget stateWidget = BusStateWidget_.build(ctx);
                        stateWidget.setBusAndTime(state.getVno(), state.getTime());
                        addView(stateWidget);
                    }
                }
        }
    }

    /**
     * if is the site and state match the stop;
     *
     * @param site
     * @param state
     * @return
     */
    private boolean isMatchStop(BusSiteModel site, BusStateModel state) {

        if (state.getNearestBusStop().equals(site.getSite())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * if is the site and state match the direction;
     *
     * @param site
     * @param state
     * @return
     */
    private boolean isMatchDirection(BusSiteModel site, BusStateModel state) {

        final String BOTH_DIRECTION = "BOTH_DIRECT";
        final String UP_DIRECTION = "UP_DIRECT";
        final String DOWN_DIRECTION = "DOWN_DIRECT";

        if (site.getDirection().equals(BOTH_DIRECTION)) {
            return true;
        } else if (state.getDirection().equals("up") && site.getDirection().equals(UP_DIRECTION)) {
            return true;
        } else if (state.getDirection().equals("down") && site.getDirection().equals(DOWN_DIRECTION)) {
            return true;
        }

        return false;
    }

}

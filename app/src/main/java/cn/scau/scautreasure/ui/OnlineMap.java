package cn.scau.scautreasure.ui;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;


import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;


import cn.scau.scautreasure.Constants;
import cn.scau.scautreasure.R;

/**
 * Created by macroyep on 2/6/15.
 * Time:14:37
 */
@EActivity
@OptionsMenu(R.menu.menu_map)
public class OnlineMap extends BaseActivity implements LocationSource, AMapLocationListener {

    private MapView mapView;

    private AMap aMap;

    private LocationSource.OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_map);
        setMoreButtonVisible(false);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);//必须写

        init();
        setTitleText("动态地图");
    }

    void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
//        aMap.addPolyline((new PolylineOptions()).
//                add(Constants.SHANGHAI, Constants.BEIJING, Constants.CHENGDU).width(10).setDottedLine(true).geodesic(true).color(Color.argb(255, 1, 1, 1)));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.setDottedLine(true).geodesic(true).color(Color.BLACK);
        polylineOptions.width(5);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        for (double xy[] : Constants.xy) {
            polylineOptions.add(new LatLng(xy[0], xy[1]));
        }

        aMap.addPolyline(polylineOptions);

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
//         aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.getUiSettings().setCompassEnabled(true);//指南针
        aMap.getUiSettings().setScaleControlsEnabled(true);

    }

    /**
     * 选择矢量地图/卫星地图/夜景地图事件的响应
     */
    @OptionsItem(R.id.menu_map_0)
    void map0() {
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
    }

    @OptionsItem(R.id.menu_map_1)
    void map1() {
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
    }

//    @OptionsItem(R.id.menu_map_2)
//    void map2() {
//        aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景模式
//
//    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
        }
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import cn.scau.scautreasure.AppConfig;
import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.api.FoodApi;
import cn.scau.scautreasure.helper.FoodShopHelper;
import cn.scau.scautreasure.helper.FoodShopHelper_;
import cn.scau.scautreasure.helper.FoodShopLoader;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.FoodShopModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;

/**
 * Created by apple on 14-8-24.
 */
@EService
public class FoodShopService extends Service {
    @RestService
    FoodApi foodApi;
    @Pref
    AppConfig_ appConfig;
    @Bean
    FoodShopLoader loader;
    @Bean
    FoodShopHelper helper;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            loadData();


        return super.onStartCommand(intent, flags, startId);
    }
    void stopService(){
        this.stopSelf();
    }
    @Background
    void loadData(){
        Log.i("外卖服务状态","开启");
        try {
            loader.downLoader(new FoodShopLoader.OnCallBack() {


                @Override
                public void onSucceed() {
                    Log.i("写入状态", "成功");
                    stopService();
                    Log.i("外卖服务状态", "已关闭");
                }

                @Override
                public void onError() {
                    Log.i("写入状态", "无新数据");
                    stopService();
                    Log.i("外卖服务状态", "已关闭");
                }
            });
        }catch (HttpStatusCodeException e) {
        Log.i("异常",String.valueOf(e.getStatusCode().value()));
//        showErrorResult(getSherlockActivity(), e.getStatusCode().value());
    } catch (Exception e) {
//        handleNoNetWorkError(getSherlockActivity());
    }
    }
//
//    @Background
//    void downLoader() {
//        //时间戳格式:1,408,760,358
//        //服务开启1,408,844,685,618
//
//       shopList = foodApi.getFoodShop(appConfig.lastUpdateFood().get());
//        Log.i("保存时间戳",String.valueOf(appConfig.lastUpdateFood().get()));
//        saveShop(shopList,);
////        Log.i("获取", shopList.getShop().get(0).getShop_name());
//        //等正式使用,放开下面一句注释.
//
//        //  appConfig.lastUpdateFood().put(Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0,10)));
//
////        showTest(shopList.getShop().get(0).getShop_name());
//
//    }
//    void saveShop(FoodShopModel.ShopList list,OnCallBack onCallBack){
//        if(list.getShop()!=null){
//            for (int i=0;i<list.getShopCount();i++) {
//                //int id, String shop_name, String phone, String status, long edit_time, long lastTime
//                //int id, String food_name, float food_price, long edit_tiime, int food_shop_id, String status
//                //把从json解析出的东西中分离组成一个数据库模型写入数据库
//                FoodShopDBModel model=new FoodShopDBModel(list.getShop().get(i).getId(),list.getShop().get(i).getShop_name(),list.getShop().get(i).getPhone()
//                ,list.getShop().get(i).getStatus(),list.getShop().get(i).getEdit_time(),0);
//                Log.i("写入/更新 店铺",list.getShop().get(i).getShop_name());
//                helper.addOrUpdateOneFoodShop(model);
//            }
//        }
//        if(list.getMenu()!=null) {
//            for (int i = 0; i < list.getMenuCount(); i++) {
//                ShopMenuDBModel model1 = new ShopMenuDBModel(list.getMenu().get(i).getId(), list.getMenu().get(i).getFood_name(), list.getMenu().get(i).getFood_price(),
//                        list.getMenu().get(i).getEdit_time(), list.getMenu().get(i).getFood_shop_id(), list.getMenu().get(i).getStatus());
//                helper.addOrUpdateMenu(model1);
//                Log.i("写入/更新 菜单", list.getMenu().get(i).getFood_name());
//            }
//        }
//        Log.i("写入状态","成功");
//        this.stopSelf();
//        Log.i("外卖服务状态","已关闭");
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.i("外卖服务状态","已Destroy");
//
//
//    }
//
//    @UiThread
//    void showTest(String text) {
//        Toast.makeText(getApplication(), text, Toast.LENGTH_LONG).show();
//    }
//    public interface OnCallBack{
//        public void onSucceed();
//        public void onError();
//    }
}

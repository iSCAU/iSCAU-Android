package cn.scau.scautreasure.helper;

import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpStatusCodeException;

import java.sql.Date;
import java.util.concurrent.ExecutionException;

import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.api.FoodApi;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.FoodShopModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;

/**
 * Created by apple on 14-8-24.
 */
@EBean
public class FoodShopLoader {
    @RestService
    FoodApi foodApi;
    @Pref
    AppConfig_ appConfig;

    FoodShopModel.ShopList list;


    @Bean
    FoodShopHelper helper;


    public  void downLoader(OnCallBack onCallBack) {
        //时间戳格式:1,408,760,358
        //服务开启1,408,844,685,618
                list = foodApi.getFoodShop(appConfig.lastUpdateFood().get());
                if (list.getShop().size() > 0) {
                    for (int i = 0; i < list.getShopCount(); i++) {
                        //int id, String shop_name, String phone, String status, long edit_time, long lastTime
                        //int id, String food_name, float food_price, long edit_tiime, int food_shop_id, String status
                        //把从json解析出的东西中分离组成一个数据库模型写入数据库
                        if (Integer.parseInt(list.getShop().get(i).getStatus()) == 1) {
                            FoodShopDBModel model = new FoodShopDBModel(list.getShop().get(i).getId(), list.getShop().get(i).getShop_name(), list.getShop().get(i).getPhone()
                                    , list.getShop().get(i).getStatus(), list.getShop().get(i).getEdit_time(), System.currentTimeMillis(), list.getShop().get(i).getStart_time(), list.getShop().get(i).getEnd_time(), list.getShop().get(i).getLogo_url());

                            helper.addOrUpdateOneFoodShop(model);
                            Log.i("写入/更新 店铺", list.getShop().get(i).getShop_name());
                        } else {
                            //如果status为0,删除该店铺
                            FoodShopDBModel model = new FoodShopDBModel(list.getShop().get(i).getId());
                            helper.deleteShop(model);
                            Log.i("删除 店铺", list.getShop().get(i).getShop_name());

                        }
                    }
                } else {
                    onCallBack.onError();
                }
                if (list.getMenu().size() > 0) {
                    appConfig.lastUpdateFood().put(Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10)));

                    for (int i = 0; i < list.getMenuCount(); i++) {
                        if (Integer.parseInt(list.getMenu().get(i).getStatus()) == 1) {
                            ShopMenuDBModel model1 = new ShopMenuDBModel(list.getMenu().get(i).getId(), list.getMenu().get(i).getFood_name(), list.getMenu().get(i).getFood_price(),
                                    list.getMenu().get(i).getEdit_time(), list.getMenu().get(i).getFood_shop_id(), list.getMenu().get(i).getStatus());
                            helper.addOrUpdateMenu(model1);
                            Log.i("写入/更新 菜单", list.getMenu().get(i).getFood_name());
                        } else {
                            ShopMenuDBModel model = new ShopMenuDBModel(list.getMenu().get(i).getId());
                            helper.deleteMenu(model);
                            Log.i("删除 菜单", list.getMenu().get(i).getFood_name());
                        }

                    }
                } else {
                    onCallBack.onError();
                }
                appConfig.lastUpdateFood().put(Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10)));
                Log.i("保存时间戳", String.valueOf(appConfig.lastUpdateFood().get()));
                onCallBack.onSucceed();



    }



    public interface OnCallBack{
        public void onSucceed();
        public void onError();
    }
}

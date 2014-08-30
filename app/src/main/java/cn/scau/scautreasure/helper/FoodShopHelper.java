package cn.scau.scautreasure.helper;

import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.sql.SQLException;
import java.util.List;

import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.api.FoodApi;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.FoodShopModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;

/**
 * Created by apple on 14-8-24.
 */
@EBean
public class FoodShopHelper {

    @OrmLiteDao(helper = DatabaseHelper.class, model = FoodShopDBModel.class)
    RuntimeExceptionDao<FoodShopDBModel, Integer> dao;
    @OrmLiteDao(helper = DatabaseHelper.class,model = ShopMenuDBModel.class)
    RuntimeExceptionDao<ShopMenuDBModel,Integer> dao1;

    public void addOrUpdateOneFoodShop(FoodShopDBModel model) {
        dao.createOrUpdate(model);
    }

    public List<FoodShopDBModel> getFoodShopList() {
        return dao.queryForAll();
    }

    public void addOrUpdateMenu(ShopMenuDBModel model) {
        dao1.createOrUpdate(model);
    }

    public List<ShopMenuDBModel> getMenuList(String fieldName,String fieldValue) {
        return dao1.queryForEq(fieldName,fieldValue);
    }
    public void deleteShop(FoodShopDBModel model){

            dao.delete(model);

    }
    public void deleteMenu(ShopMenuDBModel model){

        dao1.delete(model);

    }


}

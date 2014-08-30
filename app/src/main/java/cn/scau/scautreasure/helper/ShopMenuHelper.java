package cn.scau.scautreasure.helper;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.util.List;

import cn.scau.scautreasure.model.ShopMenuDBModel;

/**
 * Created by apple on 14-8-24.
 */
@EBean
public class ShopMenuHelper {
    @OrmLiteDao(helper = DatabaseHelper.class, model = ShopMenuDBModel.class)
    RuntimeExceptionDao<ShopMenuDBModel, Integer> dao;

    public void addOrUpdateOneFoodShop(ShopMenuDBModel model) {
        dao.createOrUpdate(model);
    }

    public List<ShopMenuDBModel> getFoodShopList() {
        return dao.queryForAll();
    }

}

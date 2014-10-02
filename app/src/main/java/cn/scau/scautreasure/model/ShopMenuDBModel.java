package cn.scau.scautreasure.model;

/**
 * Created by apple on 14-8-24.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 外卖店模型
 * "id": "1",
 * "shop_name": "一心一意",
 * "phone": "123456",
 * "status": "1",
 * "edit_time": "1408760358"
 * <p/>
 * <p/>
 * "id": "1",
 * "food_name": "焖鸡饭",
 * "food_price": "13",
 * "edit_time": "1406950250",
 * "food_shop_id": "1",
 * "status": "1"
 */
@DatabaseTable(tableName = "shopmenu")
public class ShopMenuDBModel implements Serializable{
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String food_name;
    @DatabaseField
    private float food_price;
    @DatabaseField
    private long edit_tiime;
    @DatabaseField
    private int food_shop_id;
    @DatabaseField
    private String status;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count=0;

    public ShopMenuDBModel(int id, String food_name, float food_price, long edit_tiime, int food_shop_id, String status) {
        this.id = id;
        this.food_name = food_name;
        this.food_price = food_price;
        this.edit_tiime = edit_tiime;
        this.food_shop_id = food_shop_id;
        this.status = status;
    }

    public ShopMenuDBModel(int id) {
        this.id = id;
    }

    public ShopMenuDBModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public float getFood_price() {
        return food_price;
    }

    public void setFood_price(float food_price) {
        this.food_price = food_price;
    }

    public long getEdit_tiime() {
        return edit_tiime;
    }

    public void setEdit_tiime(long edit_tiime) {
        this.edit_tiime = edit_tiime;
    }

    public int getFood_shop_id() {
        return food_shop_id;
    }

    public void setFood_shop_id(int food_shop_id) {
        this.food_shop_id = food_shop_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package cn.scau.scautreasure.model;

/**
 * Created by apple on 14-8-24.
 */

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

@DatabaseTable(tableName = "foodshop")
public class FoodShopDBModel {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String shop_name;
    @DatabaseField
    private String phone;
    @DatabaseField
    private String status;
    @DatabaseField
    private long edit_time;
    @DatabaseField
    private long lastTime;

    @DatabaseField
    private String start_time;

    @DatabaseField
    private String end_time;


    @DatabaseField
    private String logo_url;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public FoodShopDBModel(int id, String shop_name, String phone, String status, long edit_time, long lastTime, String start_time, String end_time, String logo_url) {
        this.id = id;
        this.shop_name = shop_name;
        this.phone = phone;
        this.status = status;
        this.edit_time = edit_time;
        this.lastTime = lastTime;
        this.start_time = start_time;
        this.end_time = end_time;
        this.logo_url = logo_url;
    }

    public FoodShopDBModel(int id) {
        this.id = id;
    }

    public FoodShopDBModel() {
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(long edit_time) {
        this.edit_time = edit_time;
    }
}

package cn.scau.scautreasure.model;

import java.util.ArrayList;
import java.util.List;

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
public class FoodShopModel {
    //外卖店
    private int id;
    private String shop_name;
    private String phone;
    private String status;
    private long edit_time;
    private String start_time;
    private String end_time;
    private String logo_url="http://avatar.csdn.net/0/0/D/1_vipzjyno1.jpg";//测试用

    public FoodShopModel() {
    }





    //菜单特有
    private String food_name;
    private float food_price;
    private int food_shop_id;


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

    public void setEdit_time(long edit_time) {
        this.edit_time = edit_time;
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

    public int getFood_shop_id() {
        return food_shop_id;
    }

    public void setFood_shop_id(int food_shop_id) {
        this.food_shop_id = food_shop_id;
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

    public void setEdit_time(int edit_time) {
        this.edit_time = edit_time;
    }


    public class ShopList {
        private List<FoodShopModel> shop;
        private List<FoodShopModel> menu;

        public int getShopCount() {
            return shop.size();
        }

        public int getMenuCount(){
            return menu.size();
        }
        private String status;

        public List getAll() {
            List list = new ArrayList();
            list.add(shop);
            list.add(menu);
            return list;
        }

        public List<FoodShopModel> getMenu() {
            return menu;
        }

        public void setMenu(List<FoodShopModel> menu) {
            this.menu = menu;
        }


        public List<FoodShopModel> getShop() {
            return shop;
        }

        public void setShop(List<FoodShopModel> shop) {
            this.shop = shop;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


}

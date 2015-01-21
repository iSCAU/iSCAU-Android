package cn.scau.scautreasure.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;

import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.FoodShopHelper;
import cn.scau.scautreasure.helper.LogCenter;
import cn.scau.scautreasure.model.ShopMenuDBModel;
import cn.scau.scautreasure.widget.AppOKCancelDialog;
import cn.scau.scautreasure.widget.AppToast;

/**
 * Created by apple on 14-8-24.
 */
@EActivity(R.layout.activity_shop_menu)
public class ShopMenu extends BaseActivity {
    @Click(R.id.back)
    void onBack() {
        showTips();
    }


    @Extra("")
    String id;
    @Extra("")
    String shop_name;
    @Extra("")
    String phone;
    @Extra("")
    String edit_time;
    @Extra
    String shop_logo;

    @Extra
    String start_time;
    @Extra
    String end_time;
    @ViewById(R.id.foodMenu)
    ListView lvMenu;
    FoodMenuAdapter fmAdapter;
    @Bean
    FoodShopHelper helper;

    @ViewById(R.id.shop_title)
    TextView shop_title;

    private boolean isRest = false;
    List<ShopMenuDBModel> menuList, orderList;
    private String msg = "";
    private boolean hasOrder = false;


    @Click(R.id.more)
    void next() {
        //是否休息中
        if (!isRest) {
            //产生菜色list
            makeList();
            //如果菜色不为0
            if ((orderList.size() > 0)) {
                OrderFood_.intent(this).sendList(orderList).msg(msg).phone(phone).shopName(shop_name).shopLogo(shop_logo).shopId(id).startForResult(1111);
            } else {
                AppToast.show(this, "你还没选择饭菜", 0);
            }
        } else {
            AppToast.show(this, "本外卖店休息中", 0);
        }
    }

    private void checkoutTime(String start, String end) {
        String startTime[] = start.split(":");
        String endTime[] = end.split(":");
        int startMin = Integer.parseInt(startTime[0]) * 60 + Integer.parseInt(startTime[1]);
        int endMin = Integer.parseInt(endTime[0]) * 60 + Integer.parseInt(endTime[1]);
        java.util.Calendar c = java.util.Calendar.getInstance();
        int nowMIn = c.get(java.util.Calendar.HOUR_OF_DAY) * 60 + c.get(java.util.Calendar.MINUTE);
        if (!(nowMIn > startMin && nowMIn < endMin)) {
            isRest = true;
            setTitleText("休息中");
            more.setVisibility(View.GONE);
        }
    }

    /*
    鸡扒饭 1份,鸡腿饭 3份,送到北京[来自华农宝客户端]----菜名1 份数,菜名2 份数,…。送到:地址。[来自华农宝客户端]
     */
    private void makeList() {
        orderList = new ArrayList<ShopMenuDBModel>();
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getCount() > 0) {
                msg += menuList.get(i).getFood_name() + " " + menuList.get(i).getCount() + "份,";
                orderList.add(menuList.get(i));
            }
        }
        Log.i("订餐菜数:", String.valueOf(orderList.size()));
    }

    @AfterViews
    void initView() {
        setMoreButtonText("下一步");
        setTitleText("菜单");
        shop_title.setText(shop_name);
        checkoutTime(start_time, end_time);

        loadData();
        fmAdapter = new FoodMenuAdapter();
        lvMenu.setAdapter(fmAdapter);
        Log.i("当前快餐店:", id + "|" + shop_name + "|" + phone + "|" + edit_time);
    }

    void loadData() {
        menuList = helper.getMenuList("food_shop_id", id);
        Log.i("数据数量", String.valueOf(menuList.size()));


    }

    //方便起见就不写成独立adapter
    class FoodMenuAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public Object getItem(int i) {
            return menuList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(ShopMenu.this).inflate(R.layout.food_menu_list_item_layout, null);
                viewHolder.removeOne = (ImageButton) view.findViewById(R.id.removeOne);
                viewHolder.addOne = (ImageButton) view.findViewById(R.id.addOne);
                viewHolder.foodName = (TextView) view.findViewById(R.id.foodName);
                viewHolder.foodCount = (TextView) view.findViewById(R.id.foodCount);
                viewHolder.foodMoney = (TextView) view.findViewById(R.id.foodMoney);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.foodName.setText(menuList.get(i).getFood_name());
            viewHolder.foodMoney.setText("￥" + menuList.get(i).getFood_price());
            viewHolder.foodCount.setText(String.valueOf(menuList.get(i).getCount()));
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.removeOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (menuList.get(i).getCount() > 0) {
                        menuList.get(i).setCount(menuList.get(i).getCount() - 1);
                        finalViewHolder.foodCount.setText(String.valueOf(menuList.get(i).getCount()));
                    }
                }
            });
            viewHolder.addOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    menuList.get(i).setCount(menuList.get(i).getCount() + 1);
                    finalViewHolder.foodCount.setText(String.valueOf(menuList.get(i).getCount()));
                }
            });

            return view;
        }

        class ViewHolder {
            public ImageButton removeOne;
            public ImageButton addOne;
            public TextView foodName;
            public TextView foodCount;
            public TextView foodMoney;
        }

    }


    @OnActivityResult(1111)
    void emptyMsg() {
        msg = "";
        hasOrder = true;

    }


    void showTips() {
        makeList();
        if (orderList.size() > 0 && !hasOrder && !isRest) {
//           你未进行结算，返回的话，你现在的订单会丢失，是否继续？
            AppOKCancelDialog.show(this, "提示", "你未进行结算，返回的话，你现在的订单会丢失，确定继续？", "确定", "取消", new AppOKCancelDialog.Callback() {
                @Override
                public void onCancel() {
                    LogCenter.i(getClass(), Thread.currentThread(), "用户返回导致订单丢失");
                }

                @Override
                public void onOk() {
                    finish();
                }
            });

        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            showTips();
        }
        // return super.onKeyDown(keyCode, event);
        return false;
    }

}
package cn.scau.scautreasure.ui;

import android.app.*;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

import cn.scau.scautreasure.AppConfig;
import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.OrderListAdapter;
import cn.scau.scautreasure.helper.FoodShopHelper;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;
import cn.scau.scautreasure.service.NotifyFoodService_;
import cn.scau.scautreasure.util.TextUtil;
import cn.scau.scautreasure.widget.ParamWidget;

/**
 * Created by apple on 14-8-25.
 */
@EActivity(R.layout.activity_order_food)
public class OrderFood extends CommonActivity {
    @Bean
    FoodShopHelper helper;
    @Extra
    String shopId;
    @Extra
    List<ShopMenuDBModel> sendList;
    @ViewById(R.id.orderList)
    ListView orderList;
    @Extra("")
    String msg;
    @Extra("")
    String phone;
    @Extra("")
    String shopName;
    @Extra
    String shopLogo;
    @ViewById(R.id.allMoney)
    TextView allMoney;
    @Extra("")
    String url;
    @ViewById(R.id.shopLogo)
    ImageView iv;
    @ViewById(R.id.shopName)
    TextView result;
    @ViewById
    ParamWidget param_block;
    private String[] block = {"五山区", "华山区", "启林区"};
    @ViewById(R.id.addressFull)
    EditText address;

    @Pref
    cn.scau.scautreasure.AppConfig_ appConfig;

    OrderListAdapter orderListAdapter;
    private int type;

    @Click(R.id.done_order)
    void next() {
        //保存用户输入的地址
        saveAddress();
        //判断是否手机设备,如果是则跳到发短信,否则提示
        if (!appConfig.isThePad().get()) {
            nextStep();
        } else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("请确认你的设备是手机!");
            builder.setNegativeButton("取消",null);
            builder.setPositiveButton("下一步",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    appConfig.forceMobile().put(true);//用户认为是手机，记住用户的选择
                    nextStep();
                }
            });
            builder.create();
            builder.show();
        }
    }

    void nextStep(){
        //是否填入地址
        if (!TextUtils.isEmpty(address.getText().toString().trim())) {
            //是否点到菜
            if (sendList.size() > 0) {
                String sendMsg ="[华农宝]"+ msg + "送到" + block[param_block.getWheel().getCurrentItem()] + " " + address.getText().toString().trim()  ;
                sendSMS(sendMsg);//发送短信
                type=0;

            } else {
                Toast.makeText(this, "你尚未选择饭菜", Toast.LENGTH_SHORT).show();
            }
        } else {
            AppMsg.makeText(this, "请输入详细地址", AppMsg.STYLE_ALERT).show();
        }
    }
    //更新lasttime,提供外卖店排序的根据
    void updateLastTime() {

        String lastOrderInfo = shopId+"##"+shopName+"##"+System.currentTimeMillis()+"##"+msg+"##"+type;
        appConfig.lastOrderInfo().put(lastOrderInfo);
        System.out.println("缓存:" + lastOrderInfo);
//开启同步服务
        NotifyFoodService_.intent(this).start();

        Log.i("更新外卖店:", "id=" + shopId);
        long time = System.currentTimeMillis();
        helper.updateOnFoodShop("lastTime", String.valueOf(time), Integer.valueOf(shopId));
        Log.i("更新外卖店", "更新lastTime:" + time);
    }


    //保存地址
    void saveAddress() {
        appConfig.block().put(param_block.getWheel().getCurrentItem());
        appConfig.address().put(address.getText().toString().trim());

    }

    @AfterViews
    void initView() {
        setTitle("订单确认");
        param_block.initView("校区选择", block, 0);
        param_block.getWheel().setCurrentItem(appConfig.block().get());
        address.setText(appConfig.address().get());
        result.setText(shopName);
        allMoney.setText("¥ " + countMoney());
        initListView();
        AppContext.loadImage(shopLogo, iv, null);
        Log.i("获取list的size", String.valueOf(sendList.size()));
        Log.i("logo地址", shopLogo);
//        Java代码  (List<YourObject>) getIntent().getSerializable(key)

    }

    void initListView() {
        orderListAdapter = new OrderListAdapter(this, sendList);
        orderList.setAdapter(orderListAdapter);
    }

    String countMoney() {
        float money = 0.00f;
        for (int i = 0; i < sendList.size(); i++) {
            money += sendList.get(i).getCount() * sendList.get(i).getFood_price();
        }
        DecimalFormat df = new DecimalFormat();
        String style = "0.00";//定义要显示的数字的格式
        df.applyPattern(style);// 将格式应用于格式化器
        Log.i("格式化money前后:", "前:" + money + ",后:" + df.format(money));
        return df.format(money);

    }

    private void sendSMS(String msg)

    {
        updateLastTime();//更新lastTime
        Log.i("发送的信息:", msg);
       /* Uri deleteUri = Uri.parse("content://sms");
        getContentResolver().delete(deleteUri, "type=? and address=?", new String[] {String.valueOf(3),phone});*/
        /*getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(getResultCode()== Activity.RESULT_OK){
                    Log.d("callback_phone",intent.getStringExtra("phoneNumber"));
                    Log.d("callback_send_status","success");
                    getApplicationContext().unregisterReceiver(this);
                }
            }
        },new IntentFilter("SENT_SMS_ACTION"));*/
        finish();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", phone);
        intent.putExtra("sms_body", msg);
        intent.setData(Uri.parse("smsto:"+phone)); //yes, you need the number twice


       /* Uri smsToUri = Uri.parse("smsto:" + phone);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", msg);*/

        startActivity(intent);

    }

}

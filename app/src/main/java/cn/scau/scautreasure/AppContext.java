package cn.scau.scautreasure;

import android.app.Activity;
import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.fb.push.FBMessage;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.api.SchoolActivityApi;
import cn.scau.scautreasure.helper.AppUIMeasure;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.LogCenter;
import cn.scau.scautreasure.helper.NetUtil;
import cn.scau.scautreasure.helper.NetworkHelper;
import cn.scau.scautreasure.ui.Login_;
import cn.scau.scautreasure.util.CryptUtil;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.BadgeView;

/**
 * AppContext.
 * User:  Special Leung
 * Date:  13-7-26
 * Time:  下午8:27
 * Mail:  specialcyci@gmail.com
 */
@EApplication
public class AppContext extends Application {
    //网络检测
    @Bean
    public NetworkHelper net;

    //软件本地配置
    @Pref
    public static AppConfig_ config;


    private ViewTreeObserver vto;

    @Bean
    public DateUtil dateUtil;


    public static String userName;
    public static String eduSysPassword;
    public static String libPassword;
    public static String cardPassword;
    public static String deviceToken;


    /**
     * 根据返回的requestCode,处理整个流程一般错误;
     * 405:学号不存在
     * 406:密码错误
     * 407:图书馆密码错误
     * 408:图书馆续借失败,续借失败，已达到续借上限
     * 409:校园卡密码错误
     * 500:正方挂了
     * 0:未知错误
     *
     * @param requestCode
     */
    public static void showError(int requestCode, final Activity act) {
        try {
            AppException appException = new AppException();
            appException.parseException(requestCode, act);
        } catch (AppException e) {
            Log.i("出错", "错误代码:" + e.getType());
            if (e.getType() == 405 || e.getType() == 406 || e.getType() == 407 || e.getType() == 409) {
                AppToast.showWithIntent(act, e.getMessage(), "去修改", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Login_.intent(act).start();
                    }
                }, 0);
            } else {
                AppToast.show(act, e.getMessage(), 0);

            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        FeedbackPush.getInstance(this).init(true);

        compatiable();
        getAccountSettings();
        initImageLoader(getApplicationContext());
    }


    private void compatiable() {
        System.out.println("update compatiable");
        AppCompatible appCompatible = AppCompatible_.getInstance_(this);
        appCompatible.upgrade();
    }

    @Background
    public void getAccountSettings() {
        CryptUtil cryptUtil = new CryptUtil();
        userName = config.userName().get();
        eduSysPassword = cryptUtil.decrypt(config.eduSysPassword().get());
        libPassword = cryptUtil.decrypt(config.libPassword().get());
        cardPassword = cryptUtil.decrypt(config.cardPassword().get());
        deviceToken = config.device_token().get();
    }

    public String getEncodeEduSysPassword() {
        return CryptUtil.base64_url_safe(eduSysPassword);
    }

    public String getEncodeLibPassword() {
        return CryptUtil.base64_url_safe(libPassword);
    }

    public String getEncodeCardPassword() {
        return CryptUtil.base64_url_safe(cardPassword);
    }

    public void Log(int var) {
        Log("" + var);
    }

    public void Log(long var) {
        Log("" + var);
    }

    public void Log(String log) {
        if (BuildConfig.DEBUG) {
            Log.d("App", log);
        }
    }
//配置加载图片的配置
//    private static  DisplayImageOptions  options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.loading_pic)
//            .showImageForEmptyUri(R.drawable.loading_pic)
//            .showImageOnFail(R.drawable.loading_pic)
//            .cacheInMemory(true)
//            .cacheOnDisk(true)
//            .considerExifParams(true)
//            .displayer(new RoundedBitmapDisplayer(20))
//            .build();

    public static void loadImage(String url, ImageView iv, ImageLoadingListener imageLoadingListener) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading_pic)
                .showImageForEmptyUri(R.drawable.loading_pic)
                .showImageOnFail(R.drawable.loading_pic).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示

                .imageScaleType(ImageScaleType.NONE).build();

        ImageLoader.getInstance().displayImage(url, iv, options, imageLoadingListener);
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheExtraOptions(5000, 5000)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app

                .build();


        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    /**
     * 用于设置角标红点
     *
     * @param context
     * @param view
     *
     * @return
     */
    public static BadgeView setTabRedPoint(Context context, View view) {
        BadgeView badgeView = new BadgeView(context, view);
        badgeView.setHeight(6);
        badgeView.setWidth(6);
        badgeView.setBadgeMargin(40, 7);
        badgeView.setBackgroundResource(R.drawable.redpoin);
        return badgeView;
    }

    final private static String NOTIFY_FOOD_URL = "http://iscaucms.sinaapp.com/index.php/Api/notifyFood";

    public static boolean notifyFood(Context context) {
        String info[] = null;
        if (!config.lastOrderInfo().get().equals("")) {
//            shop_id|shop_name|time
            info = config.lastOrderInfo().get().split("##");
            System.out.println(info[0]);
            System.out.println(info[1]);
            System.out.println(info[2]);
            System.out.println(info[3]);
            System.out.println(info[4]);

            long theTime = (System.currentTimeMillis() - Long.parseLong(info[2])) / 1000 / 3600;
            System.out.println(theTime + "===" + config.lastOrderInfo().get());
            if (theTime <= 1) {
                System.out.println("需要post外卖内容");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.clear();
                nameValuePairs.add(new BasicNameValuePair("text", info[3]));
                nameValuePairs.add(new BasicNameValuePair("type", info[4]));
                nameValuePairs.add(new BasicNameValuePair("shop_id", info[0]));
                nameValuePairs.add(new BasicNameValuePair("shop_name", info[1]));
                nameValuePairs.add(new BasicNameValuePair("time", String.valueOf(Long.parseLong(info[2]) / 1000)));
                JSONObject retJson = NetUtil.getResponseForPost(NOTIFY_FOOD_URL, nameValuePairs, context);
//               {"result":"success"}
                try {
                    if (retJson != null)
                        if (retJson.getString("result").equals("success")) {
                            System.out.println("成功post");
                            config.lastOrderInfo().put("");//清空最后一次点餐记录
                            return true;
                        } else {
                            System.out.println("异常post");
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("超时,不post了");
            }
        }


        return false;
    }

    public int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }



}

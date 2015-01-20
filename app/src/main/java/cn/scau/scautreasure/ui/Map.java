package cn.scau.scautreasure.ui;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.AppContext;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.widget.AppToast;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by apple on 14-8-30.
 */
@EActivity(R.layout.activity_map)
public class Map extends BaseActivity {
    private static PhotoViewAttacher mAttacher = null;
    @ViewById(R.id.map)
    ImageView map;
    private float angel = 0;


    @AfterViews
    void initView() {

        setTitleText("校内地图");
        setMoreButtonText("分享");
        setMoreButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppToast.show(Map.this, "正在开发...", 0);
            }
        });
//        AppContext.loadImage( "http://img3.imgtn.bdimg.com/it/u=86040403,2916800900&fm=90&gp=0.jpg",map,new ImageLoadingListener() {
        AppContext.loadImage("assets://map.png", map, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                Log.i("加载地图", "开始加载");
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                Log.i("加载地图", "失败");
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Log.i("加载地图", "完成");
                mAttacher = new PhotoViewAttacher(map);
                mAttacher.canZoom();
                Log.i("装载缩放", "完成");


                mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    //
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        Log.i("点击地图", "(" + x + "," + y + ")");
                    }
                });
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                Log.i("加载地图", "取消");
            }
        });
    }


}

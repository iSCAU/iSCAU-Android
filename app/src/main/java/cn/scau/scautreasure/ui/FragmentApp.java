package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.AppUIMeasure;
import cn.scau.scautreasure.helper.CacheHelper;
import cn.scau.scautreasure.helper.HttpLoader;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.FunctionModel;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.BadgeView;
import cn.scau.scautreasure.widget.ItemButton;


@EFragment(R.layout.menu)
public class FragmentApp extends BaseFragment {

    @ViewById(R.id.menu_listLayout)
    LinearLayout menu_listLayout;

    //外卖
    @ViewById(R.id.menu_kfc)
    Button menu_kfc;

    @Click(R.id.menu_kfc)
    void kfc() {
        Food_.intent(getActivity()).start();
    }


    //地图
    @ViewById(R.id.menu_map)
    Button menu_map;

    //常用信息
    @ViewById(R.id.menu_info)
    Button menu_info;

    //空教室
    @ViewById(R.id.menu_room)
    Button menu_room;

    //成绩
    @ViewById(R.id.menu_grade)
    Button menu_grade;

    //考试
    @ViewById(R.id.menu_exam)
    Button menu_exam;

    //查书
    @ViewById(R.id.menu_search)
    Button menu_search;
    //当前借阅
    @ViewById(R.id.menu_book)
    Button menu_book;
    //历史借阅
    @ViewById(R.id.menu_record)
    Button menu_record;


    @Click(R.id.more)
    void settings() {
    }

    /**
     * 搜索图书
     */
    @Click(R.id.menu_search)
    void menu_search_book() {
        SearchBook_.intent(getActivity()).start();
    }

    /**
     * 当前借阅
     */
    @Click(R.id.menu_book)
    void menu_book() {
        BorrowedBook_.intent(getActivity()).target(UIHelper.TARGET_FOR_NOW_BORROW).start();
    }

    /**
     * 借书历史记录
     */
    @Click(R.id.menu_record)
    void menu_record() {
        BorrowedBook_.intent(getActivity()).target(UIHelper.TARGET_FOR_PAST_BORROW).start();

    }

    /**
     * 空余教室
     */
    @Click(R.id.menu_room)
    void menu_room() {
        Param_.intent(this)
                .target("emptyClassRoom")._title("查空教室")
                .targetActivity(EmptyClassRoom_.class.getName())
                .start();
    }

    /**
     * 查成绩
     */
    @Click(R.id.menu_grade)
    void menu_grade() {
        Param_.intent(this)
                .target("goal")._title("查成绩")
                .targetActivity(Goal_.class.getName())
                .start();
    }

    /**
     * 考试安排
     */
    @Click(R.id.menu_exam)
    void menu_exam() {
        Exam_.intent(this).start();
    }



    /**
     * 地图
     */
    @Click(R.id.menu_map)
    void menu_map() {
        Map_.intent(this).start();

    }



    /**
     * 常用信息
     */
    @Click(R.id.menu_info)
    void menu_info() {
        BaseBrowser_.intent(getActivity()).browser_title("常用信息").allCache("1").url("http://iscaucms.sinaapp.com/apps/webapp/common_info.php").start();

    }



    @AfterViews
    void initViews() {
        cacheHelper.setCacheKey("apps");
        if (!isAfterViews) {
            isAfterViews = true;
            System.out.println("应用");
            initButton();
            initListLayout();
            loadApps();
        }


    }


    @Bean
    CacheHelper cacheHelper;

    /**
     * 加载*
     */
    @Background
    void loadApps() {
        httpLoader.getFunctionList(new HttpLoader.NormalCallBack() {
            @Override
            public void onSuccess(Object obj) {
                FunctionModel.FunctionList appList = (FunctionModel.FunctionList) obj;
                cacheHelper.writeListToCache(appList.getItems());
                initListLayout();
            }

            @Override
            public void onError(Object obj) {

            }

            @Override
            public void onNetworkError(Object obj) {

            }
        });

    }

    ArrayList<ItemButton> list = new ArrayList<ItemButton>();

    @UiThread
    void initListLayout() {
        list.clear();
        ArrayList<FunctionModel> tempList = cacheHelper.loadListFromCache();
        if (tempList != null) {
            for (final FunctionModel model : tempList) {
                ItemButton itemButton = new ItemButton(getActivity(), model.getTitle(), model.getSubtitle(), model.getFirsttext(), getResources().getColor(R.color.theme_color));
                itemButton.setOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if("1".equals(model.getJump())){
                            if (!"".equals(model.getUrl())) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(model.getUrl()));
                                startActivity(intent);
                            } else {
                                AppToast.show(FragmentApp.this.getActivity(), "网址为空,操作失败", 0);
                            }
                        }else {
                            String tempUrl = model.getLong_url() + "&student_id=" + app.userName;
                            System.out.println(tempUrl);
                            BaseBrowser_.intent(getActivity()).browser_title(model.getTitle()).url(tempUrl).allCache(model.getAllowcache()).start();
                        }
                    }
                });

                list.add(itemButton);

            }


            menu_listLayout.removeAllViews();
            for (ItemButton itemButton : list) {
                menu_listLayout.addView(itemButton);
                Log.i(getClass().getName(), itemButton.getTv_title().getText().toString());
            }

        }
    }

    /**
     * 按钮图标的控制
     */
    void initButton() {
        Button[] menu_button = {menu_kfc, menu_map, menu_info, menu_room, menu_grade, menu_exam, menu_search, menu_book, menu_record};
        for (Button bt : menu_button) {
            Drawable[] drawables = bt.getCompoundDrawables();
            drawables[1].setBounds(0, 0, 80, 80);
            bt.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }

    }

}

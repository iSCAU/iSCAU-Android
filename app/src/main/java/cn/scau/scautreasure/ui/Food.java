package cn.scau.scautreasure.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpStatusCodeException;
import org.w3c.dom.Text;

import java.util.List;

import cn.scau.scautreasure.AppCompatible;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.FoodShopAdapter;
import cn.scau.scautreasure.adapter.NoticeAdapter;
import cn.scau.scautreasure.helper.FoodShopHelper;
import cn.scau.scautreasure.helper.FoodShopLoader;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.service.FoodShopService_;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 外卖
 */
@EFragment(R.layout.food)
public class Food extends CommonFragment implements OnTabSelectListener {
    @Bean
    FoodShopHelper helper;
    @Bean
    FoodShopLoader loader;


    @ViewById(R.id.updateError)
    LinearLayout linearLayout;

    @ViewById(R.id.text2)
    TextView restText;

    @Click(R.id.refreshButton)
    void onRefresh(){
        swipe_refresh.setRefreshing(true);
        refresh();

    }

    /**
     *  "id": "1",
     "shop_name": "一心一意",
     "phone": "123456",
     "status": "1",
     "edit_time": "1408760358"

     */


    @ItemClick(R.id.shopListView)
    void itemClick(int position){
        ShopMenu_.intent(getSherlockActivity()).id(String.valueOf(list.get(position).getId()))
                .shop_name(list.get(position).getShop_name())
                .phone(list.get(position).getPhone())
                .edit_time(String.valueOf(list.get(position).getEdit_time()))
                .shop_logo(list.get(position).getLogo_url())
                .start_time(list.get(position).getStart_time())
                .end_time(list.get(position).getEnd_time())
                .start();
    }

    @Background
    void refresh(){
        try {

            loader.downLoader(new FoodShopLoader.OnCallBack() {
                @Override
                public void onSucceed() {

                    updateUi("更新成功");


                }

                @Override
                public void onError() {
                    updateUi("暂无更新");

                }
            });
        }catch (HttpStatusCodeException e) {
            Log.i("异常",String.valueOf(e.getStatusCode().value()));
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
            closeSwipeRefresh();
        } catch (Exception e) {
            handleNoNetWorkError(getSherlockActivity());
            closeSwipeRefresh();
        }
    }

    @UiThread
    void updateUi(String text){
        loadFromDB();

        Toast.makeText(getSherlockActivity(),text,Toast.LENGTH_LONG).show();

    }
    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @ViewById
    SwipeRefreshLayout swipe_refresh;

    @ViewById(R.id.shopListView)
    ListView shopListView;
    List<FoodShopDBModel> list;
    private FoodShopAdapter listAdapter;

    @AfterViews
    void initViews() {
        setSwipeRefresh();
        swipe_refresh.setRefreshing(true);
        loadFromDB();

    }
    private void setSwipeRefresh() {
        swipe_refresh.setEnabled(false);
        // 顶部刷新的样式
        swipe_refresh.setColorScheme(R.color.swipe_refresh_1,
                R.color.swipe_refresh_2,
                R.color.swipe_refresh_3,
                R.color.swipe_refresh_4);
    }

    void loadFromDB(){
        list=  helper.getFoodShopList();
        Log.i("加载数据", String.valueOf(list.size()));
        if (list.size()>0) {
            if (listAdapter==null) {
                listAdapter = new FoodShopAdapter(getSherlockActivity(), R.layout.food_shop_list_layout, list);
                shopListView.setAdapter(listAdapter);
            }else{
                listAdapter.notifyDataSetChanged();
            }
            linearLayout.setVisibility(View.GONE);
        }else{
            AppMsg.makeText(getSherlockActivity(),"暂无数据,请尝试手动更新", AppMsg.STYLE_INFO).show();
            linearLayout.setVisibility(View.VISIBLE);
        }
        closeSwipeRefresh();
    }

    @UiThread
    void closeSwipeRefresh() {
        swipe_refresh.setRefreshing(false);
    }

    @Override
    void showSuccessResult() {

    }

    @Override
    public void onTabSelect() {
        setTitle(R.string.tab_food);
        setSubTitle(null);
    }
    @Click(R.id.menu_refresh_foodshop)
    void onClick(){
        swipe_refresh.setRefreshing(true);

    }
}

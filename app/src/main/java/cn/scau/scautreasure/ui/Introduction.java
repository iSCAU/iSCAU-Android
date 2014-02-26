package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.IntroductionAdapter;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.IntroductionModel;
import cn.scau.scautreasure.util.TextUtil;
import org.androidannotations.annotations.*;
import java.util.List;
import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

/**
 * 新生介绍，社区指南等.
 * User: special
 * Date: 13-8-26
 * Time: 下午11:35
 * Mail: specialcyci@gmail.com
 */
@EFragment ( R.layout.introduction )
public class Introduction extends Common {

    @Bean
    TextUtil textUtil;

    @ViewById
    View listView;

    private String   target;
    private int      title;
    private BaseAdapter adapter;

    @AfterViews
    void init(){
        getTheBundle();
        loadData();
        setTitle(title);
    }

    private void getTheBundle(){
        title  = getArguments().getInt("title");
        target = getArguments().getString("target");
    }

    @UiThread
    void showContent(){
        ((ListView)listView).setAdapter(adapter);
    }

    @Background
    void loadData(Object... params) {
        String fileName = "introduction/" + target + ".json";
        String context  = textUtil.getFromAssets(fileName);
        List<IntroductionModel> introList = IntroductionModel.parse(context);
        buildListViewAdapter(introList);
        showContent();

    }

    private void buildListViewAdapter(List<IntroductionModel> introList){
        IntroductionAdapter introAdapter  = new IntroductionAdapter(getSherlockActivity(), R.layout.introduction_listitem, introList);
        adapter  = UIHelper.buildEffectAdapter(introAdapter, (AbsListView) listView,EXPANDABLE_ALPHA);
    }
}
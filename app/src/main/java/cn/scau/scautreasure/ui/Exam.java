package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ExamAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.ExamModel;
import cn.scau.scautreasure.util.StringUtil;
import cn.scau.scautreasure.widget.AppProgress;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 考试情况查询
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:54
 * Mail:  specialcyci@gmail.com
 */
@EActivity(R.layout.exam)
public class Exam extends ListActivity {

    @RestService
    EdusysApi api;


    @AfterViews
    void init() {
        setTitleText("考试安排");
        setMoreButtonText("刷新");
        setMoreButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        cacheHelper.setCacheKey("exam_arrange");

        list = cacheHelper.loadListFromCache();
        buildAndShowListViewAdapter();
        loadData();
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        beforeLoadData("正在刷新", "请耐心等待,正方你懂的", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
            }
        });
        try {
            list = api.getExam(AppContext.userName, app.getEncodeEduSysPassword()).getExam();
            //模拟数据,用来测试
           /* ExamModel examModel = new ExamModel("test", "test", "12-11", "教4", "3432", "34", "嗯嗯");
           ArrayList listTest = new ArrayList();
           listTest.add(examModel);
           listTest.add(examModel);
         listTest.add(examModel);
          listTest.add(examModel);
            list = listTest;*/
            cacheHelper.setCacheKey("exam_arrange");
            cacheHelper.writeListToCache(list);
            buildAndShowListViewAdapter();
        } catch (HttpStatusCodeException e) {
            showErrorResult(e.getStatusCode().value());
        } catch (Exception e) {
            handleNoNetWorkError();
        }
        afterLoadData();
    }

    private void buildAndShowListViewAdapter() {
        ExamAdapter examadapter = new ExamAdapter(this, R.layout.exam_listitem, list);
        adapter = UIHelper.buildEffectAdapter(examadapter, (AbsListView) listView, ALPHA);
        showSuccessResult();
    }

}
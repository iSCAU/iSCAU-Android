package cn.scau.scautreasure.ui;

import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;
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

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 考试情况查询
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:54
 * Mail:  specialcyci@gmail.com
 */
@EActivity(R.layout.exam)
public class Exam extends CommonQueryActivity {

    @RestService
    EdusysApi api;

    ArrayList<String> value;

    @AfterViews
    void init() {
        setTitle(R.string.title_exam);
        setDataEmptyTips(R.string.tips_exam_null);
//        cacheHelper.setCacheKey("exam_arrange");
        cacheHelper.setCacheKey("exam_" + StringUtil.join(value, "_"));
        list = cacheHelper.loadListFromCache();
        buildAndShowListViewAdapter();
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        beforeLoadData();
        try {
            list = api.getExam(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server).getExam();
            //模拟数据,用来测试

//            ExamModel examModel = new ExamModel("傻逼", "萧哥", "12-11", "教4", "3432", "34", "嗯嗯");
//            ArrayList listTest = new ArrayList();
//            listTest.add(examModel);
//            listTest.add(examModel);
//            listTest.add(examModel);
//            listTest.add(examModel);


            cacheHelper.writeListToCache(list);
            buildAndShowListViewAdapter();
        } catch (HttpStatusCodeException e) {
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(), this);
        } catch (Exception e) {
            handleNoNetWorkError(getSherlockActivity());
        }
        afterLoadData();
    }

    private void buildAndShowListViewAdapter() {
        ExamAdapter examadapter = new ExamAdapter(getSherlockActivity(), R.layout.exam_listitem, list);
        adapter = UIHelper.buildEffectAdapter(examadapter, (AbsListView) listView, ALPHA);
        showSuccessResult();
    }

}
package cn.scau.scautreasure.ui;

import android.widget.AbsListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ExamAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 考试情况查询
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:54
 * Mail:  specialcyci@gmail.com
 */
@EActivity( R.layout.exam )
public class Exam extends CommonQueryActivity{

    @RestService
    EdusysApi api;

    @AfterViews
    void init(){
        setTitle(R.string.title_exam);
        setDataEmptyTips(R.string.tips_exam_null);
        setCacheKey("exam_arrange");
        loadListFromCache();
        buildAndShowListViewAdapter();
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        beforeLoadData();
        try{
            list = api.getExam(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server).getExam();
            writeListToCache();
            buildAndShowListViewAdapter();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(),this);
        }catch (Exception e){
            handleNoNetWorkError(getSherlockActivity());
        }
        afterLoadData();
    }

    private void buildAndShowListViewAdapter(){
        ExamAdapter examadapter = new ExamAdapter(getSherlockActivity(), R.layout.exam_listitem, list);
        adapter  = UIHelper.buildEffectAdapter(examadapter, (AbsListView) listView,ALPHA);
        showSuccessResult();
    }

}
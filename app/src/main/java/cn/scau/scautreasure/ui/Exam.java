package cn.scau.scautreasure.ui;

import android.widget.AbsListView;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ExamAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

/**
 * 考试情况查询
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:54
 * Mail:  specialcyci@gmail.com
 */
@EFragment( R.layout.exam )
public class Exam extends Common implements ServerOnChangeListener{

    @RestService
    EdusysApi api;

    @AfterInject
    void init(){
        setTitle(R.string.title_exam);
        setDataEmptyTips(R.string.tips_exam_null);
        UIHelper.getDialog(R.string.loading_exam).show();
        loadData();
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            list = api.getExam(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server).getExam();
            buildListViewAdapter();
            showSuccessResult();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(),this);
        }
    }

    private void buildListViewAdapter(){
        ExamAdapter examadapter = new ExamAdapter(getSherlockActivity(), R.layout.exam_listitem, list);
        adapter  = UIHelper.buildEffectAdapter(examadapter, (AbsListView) listView,ALPHA);
    }

    @Override
    public void onChangeServer() {
        UIHelper.getDialog(R.string.loading_exam).show();
        loadData();
    }
}
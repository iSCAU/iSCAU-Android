package cn.scau.scautreasure.ui;

/**
 * Created by zzb on 2016-2-26.
 */

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.avos.avoscloud.LogUtil;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.DynamicAdapter;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.model.FunctionModel;
import cn.scau.scautreasure.helper.CacheHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;

@EFragment(R.layout.foundlayout)
public class FoundFragment extends CommonFragment implements OnTabSelectListener {

    ArrayList<FunctionModel> functionList;
    ListView functionListView;

    @AfterViews
    void initViews() {
        functionListView = (ListView) getActivity().findViewById(R.id.dynamicListview);
        functionList = CacheHelper.loadList(getActivity(), "function");
        if (functionList != null) {
            DynamicAdapter dynamicAdapter = new DynamicAdapter(getActivity(), functionList);
            functionListView.setAdapter(dynamicAdapter);
            functionListView.setOnItemClickListener(new functionClick());
        }

    }


    class functionClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {

            try {
                BaseBrowser_.intent(getActivity())
                //CommonWebviewAct_.intent(getActivity())
                        .title(functionList.get(position).getTitle())
                        .url(functionList.get(position).getUrl())
                        .allowCache(functionList.get(position).getAllowcache())
                        .start();
            } catch (Exception e) {
                LogUtil.log.i("错误:" + e.toString());
            }
        }

    }

    @Override
    public void onTabSelect() {
        setTitle("更多");
        setSubTitle(null);
    }


}

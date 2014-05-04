package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.devspark.appmsg.AppMsg;
import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

import java.util.Arrays;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.widget.SpinnerDialog;

/**
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:38
 * Mail:  specialcyci@gmail.com
 */
@EActivity
public abstract class CommonActivity extends ActionBarActivity implements DialogInterface.OnCancelListener{

    @App
    protected AppContext   app;
    @ViewById
    protected View listView;
    protected List list;
    protected BaseAdapter  adapter;
    /**
     * 当服务器返回404(查询结果为空)的提示语;
     */
    protected int tips_empty = R.string.tips_default_null;

    @AfterInject
    void initDialog(){
        UIHelper.buildDialog(this, this);
    }

    @AfterViews
    void initActionBar(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleResource){
        getSupportActionBar().setTitle(titleResource);
    }

    protected void setDataEmptyTips(int tipsResource){
        this.tips_empty = tipsResource;
    }

    /**
     * 展示查询结果;
     */
    @UiThread
    void showSuccessResult(){
        UIHelper.getDialog().dismiss();
        ((ListView)listView).setAdapter(adapter);
    }

    protected void showErrorResult(ActionBarActivity ctx, int requestCode, ServerOnChangeListener listener){
        if (isServerError(requestCode)){
            handleServerError(ctx,listener);
        }else{
            showErrorResult(ctx, requestCode);
        }
    }

    private boolean isServerError(int requestCode){
        return requestCode == 500;
    }

    private boolean ensureActivityAvailable(Activity ctx){
        return ctx != null && !ctx.isFinishing() && !ctx.isDestroyed();
    }

    /**
     * 展示http请求异常结果
     *
     * @param requestCode
     */
    @UiThread
    void showErrorResult(ActionBarActivity ctx, int requestCode){
        if( !ensureActivityAvailable(ctx) )
            return;
        UIHelper.getDialog().dismiss();
        if(requestCode == 404){
            AppMsg.makeText(ctx, tips_empty, AppMsg.STYLE_CONFIRM).show();
        }else{
            AppContext.showError(requestCode,ctx);
        }
    }

    @UiThread
    void handleServerError(final ActionBarActivity ctx, final ServerOnChangeListener listener){
        if( !ensureActivityAvailable(ctx) )
            return;
        String[] server = ctx.getResources().getStringArray(R.array.server);
        SpinnerDialog spinner = new SpinnerDialog(ctx, Arrays.asList(server));
        spinner.setDefaultSelectPosition(AppContext.getServer() - 1);
        spinner.setMessage(ctx.getString(R.string.tips_edu_server_error));
        spinner.setDialogListener(new SpinnerDialog.DialogListener() {
            @Override
            public void select(int n) {
                AppContext.setServer(n + 1);
                listener.onChangeServer();
            }
        });
        spinner.createBuilder().create().show();
    }


    @UiThread
    void handleNoNetWorkError(ActionBarActivity ctx){
        if( !ensureActivityAvailable(ctx) )
            return;
        UIHelper.getDialog().dismiss();
        AppMsg.makeText(ctx, getString(R.string.tips_no_network), AppMsg.STYLE_ALERT).show();
    }

    /**
     * 在进行http请求时，处理用户取消请求
     *
     * @param dialog
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getSimpleName());
    }

    private String getSimpleName(){
        return getClass().getSimpleName();
    }

    /**
     * 老版本是大多数fragment, 并且主要用这个函数获取context,
     *      为了减少点代码这函数继续保留了
     *
     * @return
     */
    protected ActionBarActivity getSherlockActivity(){
        return this;
    }

    @OptionsItem(android.R.id.home)
    void home(){
        this.finish();
    }
}
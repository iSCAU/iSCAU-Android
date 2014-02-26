package cn.scau.scautreasure.ui;

import android.content.DialogInterface;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.widget.ResideMenu;
import cn.scau.scautreasure.widget.SpinnerDialog;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.devspark.appmsg.AppMsg;
import com.umeng.analytics.MobclickAgent;
import org.androidannotations.annotations.*;
import org.androidannotations.api.BackgroundExecutor;

import java.util.Arrays;
import java.util.List;

/**
 * 查询通用的Activiy;
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:38
 * Mail:  specialcyci@gmail.com
 */
@EFragment
public abstract class Common extends SherlockFragment implements DialogInterface.OnCancelListener{

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
        UIHelper.buildDialog(getSherlockActivity(), this);
    }

    @AfterInject
    void initActionBar(){
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
        actionBar.setSubtitle(null);
        actionBar.show();
    }

    protected Main_ parentActivity(){
        return (Main_) getSherlockActivity();
    }

    @Override
    public void onDestroyView() {
        parentActivity().getResideMenu().clearIgnoredViewList();
        super.onDestroyView();
    }

    protected void setTitle(String title){
        getSherlockActivity().getSupportActionBar().setTitle(title);
    }

    protected void setTitle(int titleResource){
        getSherlockActivity().getSupportActionBar().setTitle(titleResource);
    }

    protected void setDataEmptyTips(int tipsResource){
        this.tips_empty = tipsResource;
    }

    protected ResideMenu getResideMenu(){
        return parentActivity().getResideMenu();
    }

    protected void addIgnoredView(View v){
        parentActivity().getResideMenu().addIgnoredView(v);
    }

    protected boolean isMenuOpen(){
        return getResideMenu().isOpened();
    }

    protected void closeMenu(){
        getResideMenu().closeMenu();
    }

    protected void openMenu(){
        getResideMenu().openMenu();
    }

    /**
     * 展示查询结果;
     */
    @UiThread
    void showSuccessResult(){
        UIHelper.getDialog().dismiss();
        ((ListView)listView).setAdapter(adapter);
    }

    protected void showErrorResult(SherlockFragmentActivity ctx, int requestCode, ServerOnChangeListener listener){
        if (isServerError(requestCode)){
            handleServerError(ctx,listener);
        }else{
            showErrorResult(ctx, requestCode);
        }
    }

    private boolean isServerError(int requestCode){
        return requestCode == 500;
    }

    /**
     * 展示http请求异常结果
     *
     * @param requestCode
     */
    @UiThread
    void showErrorResult(SherlockFragmentActivity ctx, int requestCode){
        if(ctx == null) return;
        UIHelper.getDialog().dismiss();
        if(requestCode == 404){
            AppMsg.makeText(ctx, tips_empty, AppMsg.STYLE_CONFIRM).show();
        }else{
            AppContext.showError(requestCode,ctx);
        }
    }

    @UiThread
    void handleServerError(final SherlockFragmentActivity ctx, final ServerOnChangeListener listener){
        if(ctx == null) return;
        UIHelper.getDialog().dismiss();
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

}

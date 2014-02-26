package cn.scau.scautreasure.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.scau.scautreasure.R;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import com.actionbarsherlock.view.MenuItem;

/**
 * User: special
 * Date: 13-9-2
 * Time: 上午10:11
 * Mail: specialcyci@gmail.com
 */
@EViewGroup ( R.layout.residemenu_item )
public class ResideMenuItem extends LinearLayout{

    /** menu item  icon  */
    @ViewById ImageView iv_icon;

    /** menu item  title */
    @ViewById TextView  tv_title;

    private MenuItem menu;

    public ResideMenuItem(Context context) {
        super(context);
    }

    /**
     * set up the menu item which this need to show;
     * @param menu
     */
    public void setMenu(MenuItem menu){
        this.menu = menu;
        tv_title.setText(menu.getTitle());
        if(menu.getIcon() != null)
            iv_icon.setImageDrawable(menu.getIcon());
    }

    /**
     * return the menu item;
     * @return
     */
    public MenuItem getMenu() {
        return menu;
    }

    /**
     * return the lable's textView;
     * @return
     */
    public TextView getTv_title() {
        return tv_title;
    }

    /**
     * return the icon's imageView;
     * @return
     */
    public ImageView getIv_icon() {
        return iv_icon;
    }

    /**
     * set the icon color;
     * @param color
     */
    public void setIconColor(int color){
        iv_icon.setBackgroundColor(color);
    }

}

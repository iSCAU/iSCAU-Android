package cn.scau.scautreasure.widget;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;
import cn.scau.scautreasure.R;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * 巴士状态部件
 *
 * User: special
 * Date: 13-9-8
 * Time: 下午4:40
 * Mail: specialcyci@gmail.com
 */
@EViewGroup( R.layout.bus_state_widget )
public class BusStateWidget extends TableRow {

    @ViewById
    TextView tv_bus_state,tv_bus_time;

    public BusStateWidget(Context context) {
        super(context);
    }

    public void setBusAndTime(String busVno, String time){
        tv_bus_state.setText(busVno);
        tv_bus_time.setText(time);
    }
}

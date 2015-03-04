package cn.scau.scautreasure.ui;

import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.CalendarModel;

/**
 * Created by macroyep on 2/10/15.
 * Time:14:38
 */
@EActivity(R.layout.todays)
public class Todays extends BaseActivity {
    @ViewById(R.id.listView)
    ListView listView;
    @ViewById(R.id.tv_day)
    TextView tv_day;
    @ViewById(R.id.tv_year_month)
    TextView tv_year_month;

    @Extra
    List<CalendarModel> list;

    @AfterViews
    void init() {
        setTitleText("今天");
        setMoreButtonText("校历");
        tv_day.setText(app.dateUtil.getCurrentDay());
        tv_year_month.setText(app.dateUtil.getCurrentYearMonth() + "\n" + app.dateUtil.getWeekOfDate());
        if (list != null)
            listView.setAdapter(new MyAdapter());

    }

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        BaseBrowser_.intent(this).browser_title("校历").allCache("1").url("http://iscaucms.sinaapp.com/apps/calendar/calendar.php").start();
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(Todays.this).inflate(R.layout.title_subtitle_list_item, null);
            ((TextView) view.findViewById(R.id.title)).setText(list.get(i).getTitle());
            ((TextView) view.findViewById(R.id.subtitle)).setText(list.get(i).getContent());
            return view;
        }
    }
}

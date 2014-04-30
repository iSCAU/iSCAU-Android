package cn.scau.scautreasure.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import antistatic.spinnerwheel.WheelHorizontalView;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.CalendarHelper;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.ResideMenu;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.WeekdayArrayAdapter;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * User: special
 * Date: 13-8-27
 * Time: 下午4:57
 * Mail: specialcyci@gmail.com
 */
@EFragment ( R.layout.calendar )
public class Calendar extends Common {

    @ViewById
    WheelHorizontalView      wheel;
    @ViewById
    TextView                 tv_event;
    @ViewById
    LinearLayout layout_calendar;
    @StringArrayRes
    String[]                 months;
    @Bean
    DateUtil                 dateUtil;
    @Bean
    CalendarHelper           calendarHelper;
    private CaldroidFragment caldroidFragment;
    private int currentMonth;
    private int currentYear;
    private boolean scrolling = true;
    private HashMap<Date,CalendarHelper.Event> events;

    @AfterInject
    void init(){
        getSherlockActivity().getSupportActionBar().hide();
        setUpCurrentYearMonth();
    }

    private void setUpCurrentYearMonth(){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        currentMonth =  cal.get(cal.MONTH);
        currentYear  =  cal.get(cal.YEAR);
    }

    @Click
    void home(){
        getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public void onDestroyView() {
        getSherlockActivity().getSupportActionBar().show();
        super.onDestroyView();
    }

    @AfterViews
    void initCanlendar(){
        setUpWheels();
        setUpCalendar();
        loadData();
    }

    private void setUpWheels(){
        ArrayWheelAdapter<String> adapter_direction = new ArrayWheelAdapter<String>(getSherlockActivity(), months);
        adapter_direction.setItemTextResource(R.id.text);
        adapter_direction.setItemResource(R.layout.calendar_wheel_text);
        wheel.setViewAdapter(adapter_direction);
        wheel.setCurrentItem(currentMonth);
        wheel.setEnabled(false);
    }

    private void setUpCalendar(){

        caldroidFragment = new CaldroidFragment();
        setCalendarCurrentYearMonth();
        setCalendarFontAndListener();
        showCalendarInFragment();
        addIgnoredView(layout_calendar);
    }

    private void setCalendarCurrentYearMonth(){
        Bundle args      = new Bundle();
        args.putInt(CaldroidFragment.YEAR,  currentYear);
        args.putInt(CaldroidFragment.MONTH, currentMonth + 1);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS,false);
        caldroidFragment.setArguments(args);
    }

    private void setCalendarFontAndListener(){
        caldroidFragment.setCaldroidListener(listener);
        WeekdayArrayAdapter.textColor = getResources().getColor(R.color.calendar_weekday_textcolor);
    }

    private void showCalendarInFragment(){
        FragmentTransaction fragmentTransaction = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_calendar, caldroidFragment);
        fragmentTransaction.commit();
    }

    @UiThread
    void showEvents(){
        Iterator<Date> keySets = events.keySet().iterator();
        while (keySets.hasNext()){
            Date date = keySets.next();
            setCalendarDaySkinsByEventDate(date);
        }
        caldroidFragment.refreshView();
    }

    private void setCalendarDaySkinsByEventDate(Date date){
        CalendarHelper.Event event = events.get(date);
        caldroidFragment.setBackgroundResourceForDate(event.event_color,date);
        caldroidFragment.setTextColorForDate(android.R.color.white,date);
    }

    @Background
    void loadData(Object... params){
        events = calendarHelper.getEventList();
        showEvents();
    }

    private CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            // if user click the day, check if this day was a event day;
            if(hasThisEvenDate(date)){
                showEventNameInTitle(date);
            }else{
                showNullThiEventInTitle();
            }
        }

        @Override
        public void onChangeMonth(int month, int year) {
            int wheelPosition = month - 1;

            // if user want turn the month out of this semester
            caldroidFragment.setCaldroidListener(null);
            if(lowerThanSemesterStartMonth(year, month)){
                moveBackToSemesterStartMonth();
                wheelPosition = month;

            }else if(higherThanSemesterEndMonth(year, month)){
                moveBackToSemesterEndMonth();
                wheelPosition = month - 2;
            }

            caldroidFragment.setCaldroidListener(this);
            wheel.setCurrentItem(wheelPosition,true);
        }

    };

    private boolean hasThisEvenDate(Date date){
        return events.containsKey(date);
    }

    private void showEventNameInTitle(Date date){
        CalendarHelper.Event event = events.get(date);
        tv_event.setText(event.event_name);
    }

    private void showNullThiEventInTitle(){
        tv_event.setText(R.string.tips_calendar_null);
    }

    private boolean lowerThanSemesterStartMonth(int year, int month){
        return month < calendarHelper.MINMONTH && year == calendarHelper.MINYEAR;
    }

    private boolean higherThanSemesterEndMonth(int year, int month){
        return month > calendarHelper.MAXMONTH && year == calendarHelper.MAXYEAR;
    }

    private void moveBackToSemesterStartMonth(){
        Date date = dateUtil.parseDate(calendarHelper.MINYEAR + "-" + calendarHelper.MINMONTH + "-01");
        caldroidFragment.moveToDate(date);
    }

    private void moveBackToSemesterEndMonth(){
        Date date = dateUtil.parseDate(calendarHelper.MAXYEAR + "-" + calendarHelper.MAXMONTH + "-01");
        caldroidFragment.moveToDate(date);
    }

}
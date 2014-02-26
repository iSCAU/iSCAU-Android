package cn.scau.scautreasure.helper;

import android.content.Context;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.util.TextUtil;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * 校历的辅助类
 * User: special
 * Date: 13-8-28
 * Time: 上午11:47
 * Mail: specialcyci@gmail.com
 */
@EBean
public class CalendarHelper {

    @RootContext
    Context ctx;

    @Bean
    TextUtil textUtil;

    @Bean
    DateUtil dateUtil;

    public int MINMONTH;

    public int MAXMONTH;

    public int MINYEAR;

    public int MAXYEAR;

    /**
     * color list of calendar events;
     */
    private static int[]  colors = new int[]{
            R.color.calendar_item_vacation,
            R.color.calendar_item_has_lesson,
            R.color.calendar_item_normal
    };

    public String getTodayEventTitle(){
        HashMap<Date, Event> eventList = getEventList();
        Event event = eventList.get(dateUtil.getCurrentDate());
        if (event != null){
            return event.event_name;
        }else{
            return "";
        }
    }

    /**
     * get the school event lists,and set the settings;
     * @return
     */
    public HashMap<Date,Event> getEventList(){

        HashMap<Date,Event> eventsMap = new HashMap<Date, Event>();
        try {
            JSONObject jsonArray = new JSONObject(getContent());
            JSONObject settings = jsonArray.getJSONObject("settings");
            JSONArray  events   = jsonArray.getJSONArray("data");

            getSettings(settings);
            eventsMap = buildEachEvent(eventsMap,events);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eventsMap;
    }

    /**
     * return the event day background colors;
     * @param event_type
     * @return
     */
    private int getEventColor(String event_type){
        if (event_type.equals("vacation"))   return colors[0];
        if (event_type.equals("has_lesson")) return colors[1];
        if (event_type.equals("normal"))     return colors[2];
        return colors[0];
    }

    private void getSettings(JSONObject settings) throws JSONException {
        MINMONTH = settings.getInt("min_month");
        MINYEAR  = settings.getInt("min_year");
        MAXMONTH = settings.getInt("max_month");
        MAXYEAR  = settings.getInt("max_year");
    }

    private String getContent(){
        return textUtil.getFromAssets("calendar/Calendar.json");
    }

    private HashMap<Date,Event> buildEachEvent(HashMap<Date,Event> eventsMap, JSONArray events)
            throws JSONException {

        for (int i = 0; i < events.length(); i ++){
            JSONObject   eventJsonObject  = events.getJSONObject(i);
            String[]     date   = eventJsonObject.getString("date").split(",");
            int     event_color = getEventColor(eventJsonObject.getString("type"));

            Event event = new Event();
            event.event_name  = eventJsonObject.getString("event_name");
            event.event_color = event_color;

            for( String _date : date)
                eventsMap.put(dateUtil.parseDate(_date),event);
        }

        return eventsMap;
    }


    /**
     * entity of event;
     */
    public class Event{
        public String event_name;
        public int    event_color;
    }

}

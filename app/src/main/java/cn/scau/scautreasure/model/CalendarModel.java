package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macroyep on 2/10/15.
 * Time:13:52
 */

public class CalendarModel implements Serializable {
    private String id;
    private String title;
    private String content;
    private String start_date;
    private String end_date;
    private String type;
    private String update_time;

    public CalendarModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "CalendarModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", type='" + type + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }

    public class CalendarList {
        private List<CalendarModel> list;


        public CalendarList(List<CalendarModel> list) {
            this.list = list;
        }

        public List<CalendarModel> getList() {
            return list;
        }

        public void setList(List<CalendarModel> list) {
            this.list = list;
        }

    }
}

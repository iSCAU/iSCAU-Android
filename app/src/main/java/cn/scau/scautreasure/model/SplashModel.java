package cn.scau.scautreasure.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Splash模型
 */
@DatabaseTable(tableName = "db_splash")
public class SplashModel implements Serializable {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String url;
    @DatabaseField
    private int start_time;
    @DatabaseField
    private int end_time;
    @DatabaseField
    private int edit_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public int getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(int edit_time) {
        this.edit_time = edit_time;
    }

    @Override
    public String toString() {
        return "SplashModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", edit_time='" + edit_time + '\'' +
                '}';
    }


    public class SplashList {
        private ArrayList<SplashModel> courses;
        private String status;

        public ArrayList<SplashModel> getCourses() {
            return courses;
        }

        public void setCourses(ArrayList<SplashModel> courses) {
            this.courses = courses;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

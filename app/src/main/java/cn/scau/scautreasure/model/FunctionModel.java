package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macroyep on 2/4/15.
 * Time:16:44
 */
public class FunctionModel implements Serializable {
    private String id;
    private String firsttext;
    private String title;
    private String url;
    private String allowcache;
    private String create_time;
    private String update_time;
    private String long_url;
    private String subtitle;

    @Override
    public String toString() {
        return "FunctionModel{" +
                "id='" + id + '\'' +
                ", firsttext='" + firsttext + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", allowcache='" + allowcache + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", long_url='" + long_url + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirsttext() {
        return firsttext;
    }

    public void setFirsttext(String firsttext) {
        this.firsttext = firsttext;
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

    public String getAllowcache() {
        return allowcache;
    }

    public void setAllowcache(String allowcache) {
        this.allowcache = allowcache;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getLong_url() {
        return long_url;
    }

    public void setLong_url(String long_url) {
        this.long_url = long_url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public class FunctionList {
        private ArrayList<FunctionModel> items;

        public ArrayList<FunctionModel> getItems() {
            return items;
        }

        public void setItems(ArrayList<FunctionModel> items) {
            this.items = items;
        }
    }
}

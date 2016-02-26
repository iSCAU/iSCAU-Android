package cn.scau.scautreasure.model;

/**
 * Created by zzb on 2016-2-26.
 */
import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;

public class FunctionModel implements Serializable {

    private String id;
    private String firsttext;
    private String title;
    private String url;
    private String allowcache;
    private String update_time;

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
    public String getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public class FunctionList{

        private int state;
        private int mostShow;
        private ArrayList<FunctionModel> result;

        public int getMostShow() {
            return mostShow;
        }
        public void setMostShow(int mostShow) {
            this.mostShow = mostShow;
        }

        public int getState() {
            return state;
        }
        public void setState(int state) {
            this.state = state;
        }
        public ArrayList<FunctionModel> getResult() {
            return result;
        }
        public void setResult(ArrayList<FunctionModel> result) {
            this.result = result;
        }


    }

}


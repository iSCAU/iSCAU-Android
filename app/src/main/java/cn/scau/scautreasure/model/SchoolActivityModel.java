package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.List;

/**
 * 校园活动 模板
 */
public class SchoolActivityModel implements Serializable {
    private int id;
    private String title;
    private String place;
    private String association;
    private int level;
    private String logoUrl;
    private String username;
    private String time;
    private int t;
    private String content;
    private boolean isNewOne = false;

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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public boolean getIsNewOne(){
        return isNewOne;
    }

    public void setIsNewOne(boolean isNewOne){
        this.isNewOne = isNewOne;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    @Override
    public String toString() {
        return "SchoolActivityModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", association='" + association + '\'' +
                ", level='" + level + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", username='" + username + '\'' +
                ", time='" + time + '\'' +
                ", t='" + t + '\'' +
                '}';
    }


    public class ActivityList implements Serializable{
        private int count;
        private List<SchoolActivityModel> content;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<SchoolActivityModel> getContent() {
            return content;
        }

        public void setContent(List<SchoolActivityModel> content) {
            this.content = content;
        }
    }
}

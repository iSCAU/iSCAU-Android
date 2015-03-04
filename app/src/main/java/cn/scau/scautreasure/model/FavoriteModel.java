package cn.scau.scautreasure.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by macroyep on 2/9/15.
 * Time:11:06
 */
@DatabaseTable(tableName = "favorite")
public class FavoriteModel implements Serializable {
    public static final int ULR = 1; //url收藏
    public static final int TEXT = 0;//文本收藏
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String subtitle;

    @DatabaseField
    private String url;

    @DatabaseField
    private String content;

    //暂且支持文本收藏(url+文字)
    @DatabaseField
    private int favoriteType;

    @DatabaseField
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FavoriteModel() {
    }

    public FavoriteModel(String title, String subtitle, String url, String content, int favoriteType, String date) {
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
        this.content = content;
        this.favoriteType = favoriteType;
        this.date = date;
    }

    public int getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(int favoriteType) {
        this.favoriteType = favoriteType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FavoriteModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", favoriteType=" + favoriteType +
                ", date='" + date + '\'' +
                '}';
    }
}

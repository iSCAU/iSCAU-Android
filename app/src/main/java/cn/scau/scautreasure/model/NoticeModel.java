package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 校园公告的MODEL;
 * User: special
 * Date: 13-8-27
 * Time: 下午2:01
 * Mail: specialcyci@gmail.com
 */
public class NoticeModel implements Serializable {

    private String title;
    private String content;
    private String time;
    private String url;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NoticeModel{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public class NoticeList implements Serializable {

        private int count;
        private ArrayList<NoticeModel> notice;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ArrayList<NoticeModel> getNotice() {
            return notice;
        }

        public void setNotice(ArrayList<NoticeModel> notice) {
            this.notice = notice;
        }
    }
}

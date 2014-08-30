package cn.scau.scautreasure.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新生介绍的文本模型;
 * User: special
 * Date: 13-8-26
 * Time: 下午11:10
 * Mail: specialcyci@gmail.com
 */
public class IntroductionModel {

    private String title;
    private String content;

    public static List<IntroductionModel> parse(String str) {

        List<IntroductionModel> tList = new ArrayList<IntroductionModel>();

        try {
            JSONArray jsonArray = new JSONArray(str);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                IntroductionModel model = new IntroductionModel();
                model.setTitle(object.getString("title"));
                model.setContent(object.getString("content"));
                tList.add(model);
            }

        } catch (JSONException e) {
        }

        return tList;
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
}

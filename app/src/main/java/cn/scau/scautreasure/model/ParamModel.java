package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-8-15
 * Time:  下午4:09
 * Mail:  specialcyci@gmail.com
 */
public class ParamModel implements Serializable{

    private String key;
    private String[] value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ParamModel{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public class ParamList {

        private ArrayList<ParamModel> params;

        public ArrayList<ParamModel> getParams() {
            return params;
        }

        public void setParams(ArrayList<ParamModel> params) {
            this.params = params;
        }
    }
}

package cn.scau.scautreasure.model;

import java.io.Serializable;

/**
 * Created by apple on 14-9-5.
 */
public class ActivityCountModel implements Serializable {
//    {"result":3}
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

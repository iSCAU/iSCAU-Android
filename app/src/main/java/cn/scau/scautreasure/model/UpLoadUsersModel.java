package cn.scau.scautreasure.model;

import java.io.Serializable;

/**
 * Created by apple on 14-9-5.
 */
public class UpLoadUsersModel implements Serializable {
    private String result;

    public UpLoadUsersModel(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

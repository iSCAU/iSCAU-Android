package cn.scau.scautreasure.model;

import java.io.Serializable;

/**
 * Created by stcdasqy on 14-3-13.
 */
public class StatusModel implements Serializable {
    private String result;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult() {
        this.result = result;
    }
}

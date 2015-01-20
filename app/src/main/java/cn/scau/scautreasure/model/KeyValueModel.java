package cn.scau.scautreasure.model;

/**
 * Created by macroyep on 15/1/20.
 * Time:10:24
 */

public class KeyValueModel {
    private String key;
    private int value;

    public KeyValueModel(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
package cn.scau.scautreasure.model;

import java.util.List;

/**
 * User: special
 * Date: 13-9-21
 * Time: 下午3:36
 * Mail: specialcyci@gmail.com
 */

//        "交易发生时间" => "time",
//        "交易类型" => "type",
//        "子系统名称" => "place",
//        "交易额" => "amount",
//        "现有余额" => "balance",
//        "次数" => "frequency",
//        "状态" => "status",

public class CardRecordModel {

    private String time;
    private String type;
    private String place;
    private String amount;
    private String balance;
    private String frequency;
    private String status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CardRecordModel{" +
                "time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", place='" + place + '\'' +
                ", amount='" + amount + '\'' +
                ", balance='" + balance + '\'' +
                ", frequency='" + frequency + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public class RecordList {
        private int count;
        private String amount;
        private List<CardRecordModel> records;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public List<CardRecordModel> getRecords() {
            return records;
        }

        public void setRecords(List<CardRecordModel> records) {
            this.records = records;
        }
    }
}

package cn.scau.scautreasure.model;

import java.util.List;

/**
 * 某辆巴士的到站信息
 *
 * User: special
 * Date: 13-9-8
 * Time: 下午1:12
 * Mail: specialcyci@gmail.com
 */
public class BusStateModel {

    private String vno;
    private String direction;
    private String lineNum;
    private String nearestBusStop;
    private String powerState;
    private String time;

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getNearestBusStop() {
        return nearestBusStop;
    }

    public void setNearestBusStop(String nearestBusStop) {
        this.nearestBusStop = nearestBusStop;
    }

    public String getPowerState() {
        return powerState;
    }

    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BusStateModel{" +
                "vno='" + vno + '\'' +
                ", direction='" + direction + '\'' +
                ", lineNum='" + lineNum + '\'' +
                ", nearestBusStop='" + nearestBusStop + '\'' +
                ", powerState='" + powerState + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public class StateList {

        private List<BusStateModel> states;

        public List<BusStateModel> getStates() {
            return states;
        }

        public void setStates(List<BusStateModel> states) {
            this.states = states;
        }
    }
}

package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 校巴线路模型
 *
 * User: special
 * Date: 13-9-8
 * Time: 下午1:10
 * Mail: specialcyci@gmail.com
 */
public class BusLineModel implements Serializable {

    private String lineNum;
    private String lineType;

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    @Override
    public String toString() {
        return "BusLineModel{" +
                "lineNum='" + lineNum + '\'' +
                ", lineType='" + lineType + '\'' +
                '}';
    }


    public class LineList{
        private ArrayList<BusLineModel> lines;

        public ArrayList<BusLineModel> getLines() {
            return lines;
        }

        public void setLines(ArrayList<BusLineModel> lines) {
            this.lines = lines;
        }
    }
}

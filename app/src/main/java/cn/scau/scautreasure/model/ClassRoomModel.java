package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午6:03
 * Mail:  specialcyci@gmail.com
 */
//        "教室类别" => "type",
//        "教室编号" => "number",
//        "上课座位数" => "seat_class",
//        "考试座位数" => "seat_exam",
//        "建筑面积" => "area",
//        "预约情况" => "has_book"
public class ClassRoomModel implements Serializable {

    private String type;
    private String number;
    private String seat_class;
    private String seat_exam;
    private String area;
    private String campus;
    private String has_book;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSeat_class() {
        return seat_class;
    }

    public void setSeat_class(String seat_class) {
        this.seat_class = seat_class;
    }

    public String getSeat_exam() {
        return seat_exam;
    }

    public void setSeat_exam(String seat_exam) {
        this.seat_exam = seat_exam;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getHas_book() {
        return has_book;
    }

    public void setHas_book(String has_book) {
        this.has_book = has_book;
    }

    @Override
    public String toString() {
        return "ClassRoomModel{" +
                "type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", seat_class='" + seat_class + '\'' +
                ", seat_exam='" + seat_exam + '\'' +
                ", area='" + area + '\'' +
                ", campus='" + campus + '\'' +
                ", has_book='" + has_book + '\'' +
                '}';
    }

    public class ClassRoomList {

        private ArrayList<ClassRoomModel> classRooms;

        public ArrayList<ClassRoomModel> getClassRooms() {
            return classRooms;
        }

        public void setClassRooms(ArrayList<ClassRoomModel> classRooms) {
            this.classRooms = classRooms;
        }
    }
}

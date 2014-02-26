package cn.scau.scautreasure.model;

import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午5:33
 * Mail:  specialcyci@gmail.com
 */
//        "教师姓名" => "name_teacher",
//        "周学时" => "hours_week",
//        "起始结束周" => "week_range",
//        "上课时间" => "time",
//        "教材" => "teaching_material"
public class PickClassModel {
    private String name;
    private String name_teacher;
    private String credit;
    private String hours_week;
    private String week_range;
    private String campus;
    private String time;
    private String place;
    private String teaching_material;
    private String college_belong;
    private String classify;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_teacher() {
        return name_teacher;
    }

    public void setName_teacher(String name_teacher) {
        this.name_teacher = name_teacher;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getHours_week() {
        return hours_week;
    }

    public void setHours_week(String hours_week) {
        this.hours_week = hours_week;
    }

    public String getWeek_range() {
        return week_range;
    }

    public void setWeek_range(String week_range) {
        this.week_range = week_range;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTeaching_material() {
        return teaching_material;
    }

    public void setTeaching_material(String teaching_material) {
        this.teaching_material = teaching_material;
    }

    public String getCollege_belong() {
        return college_belong;
    }

    public void setCollege_belong(String college_belong) {
        this.college_belong = college_belong;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    @Override
    public String toString() {
        return "PickClassModel{" +
                "name='" + name + '\'' +
                ", name_teacher='" + name_teacher + '\'' +
                ", credit='" + credit + '\'' +
                ", hours_week='" + hours_week + '\'' +
                ", week_range='" + week_range + '\'' +
                ", campus='" + campus + '\'' +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", teaching_material='" + teaching_material + '\'' +
                ", college_belong='" + college_belong + '\'' +
                ", classify='" + classify + '\'' +
                '}';
    }

    public class PickClassList{

        private List<PickClassModel> pickclassinfos;

        public List<PickClassModel> getPickclassinfos() {
            return pickclassinfos;
        }

        public void setPickclassinfos(List<PickClassModel> pickclassinfos) {
            this.pickclassinfos = pickclassinfos;
        }
    }
}

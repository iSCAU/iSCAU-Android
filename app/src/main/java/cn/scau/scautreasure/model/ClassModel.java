package cn.scau.scautreasure.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * 课程表模型;
 * User:  Special Leung
 * Date:  13-7-28
 * Time:  下午11:32
 * Mail:  specialcyci@gmail.com
 */

@DatabaseTable(tableName = "classes")
public class ClassModel implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String classname;

    @DatabaseField
    private String teacher;

    @DatabaseField
    private String day;

    @DatabaseField
    private String node;

    @DatabaseField
    private int strWeek;

    @DatabaseField
    private int endWeek;

    @DatabaseField
    private String dsz;

    @DatabaseField
    private String location;

    public ClassModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getStrWeek() {
        return strWeek;
    }

    public void setStrWeek(int strWeek) {
        this.strWeek = strWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public String getDsz() {
        return dsz;
    }

    public void setDsz(String dsz) {
        this.dsz = dsz;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ClassModel{" +
                "classname='" + classname + '\'' +
                ", teacher='" + teacher + '\'' +
                ", day='" + day + '\'' +
                ", node='" + node + '\'' +
                ", strWeek='" + strWeek + '\'' +
                ", endWeek='" + endWeek + '\'' +
                ", dsz='" + dsz + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public class ClassList {

        private List<ClassModel> classes;

        private String termStartDate;

        public String getTermStartDate() {
            return termStartDate;
        }

        public void setTermStartDate(String termStartDate) {
            this.termStartDate = termStartDate;
        }

        public List<ClassModel> getClasses() {
            return classes;
        }

        public void setClasses(List<ClassModel> classes) {
            this.classes = classes;
        }
    }

}

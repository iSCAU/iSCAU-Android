package cn.scau.scautreasure.model;

/**
 * 登陆回执Model,返回个人信息;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午11:56
 * Mail:  specialcyci@gmail.com
 */

public class PersonModel {

    private String name;
    private String grade;
    private String collage;
    private String department;
    private String major;
    private String classes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCollage() {
        return collage;
    }

    public void setCollage(String collage) {
        this.collage = collage;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "PersonModel{" +
                " name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", collage='" + collage + '\'' +
                ", department='" + department + '\'' +
                ", major='" + major + '\'' +
                ", classes='" + classes + '\'' +
                '}';
    }

}

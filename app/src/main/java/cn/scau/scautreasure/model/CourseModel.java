package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stcdasqy on 14-3-13.
 */
//	"课程id" => "CourseId",
//	"课程名称" => "CourseName",
//	"任课老师" => "Teacher",
//	"课程归属" => "Property",
//	"课程学分" => "Scroe",
//	"喜欢人数" => "Liked",
//	"讨厌人数" => "Disliked"
public class CourseModel implements Serializable {
    private int CourseId;
    private String CourseName;
    private String Teacher;
    private String Property;
    private String Score;
    private int Liked;
    private int Disliked;

    public int getCourseId() {
        return CourseId;
    }

    public void setCourseId(int CourseId) {
        this.CourseId = CourseId;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String Teacher) {
        this.Teacher = Teacher;
    }

    public String getProperty() {
        return Property;
    }

    public void setProperty(String Property) {
        this.Property = Property;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String Score) {
        this.Score = Score;
    }

    public int getLiked() {
        return Liked;
    }

    public void setLiked(int Liked) {
        this.Liked = Liked;
    }

    public int getDisliked() {
        return Disliked;
    }

    public void setDisliked(int Disliked) {
        this.Disliked = Disliked;
    }

    @Override
    public String toString() {
        return "CourseModel{" +
                "CourseId=" + CourseId +
                ", CourseName='" + CourseName + '\'' +
                ", Teacher='" + Teacher + '\'' +
                ", Porperty='" + Property + '\'' +
                ", Score='" + Score + '\'' +
                ", Liked='" + Liked + '\'' +
                ", Disliked='" + Disliked + '\'' +
                '}';
    }

    public class CourseList {

        private List<CourseModel> courses;

        public List<CourseModel> getCourses() {
            return courses;
        }

        public void setCourses(List<CourseModel> courses) {
            this.courses = courses;
        }
    }

}

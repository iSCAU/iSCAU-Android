package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.List;
/**
 * 2014-3-17
 */
public class CourseModel implements Serializable {
    private int CourseId;
    private String CourseName;
    private String Teacher;
    private String Property;
    private String Score;
    private int Liked;
    private int Disliked;

    public int getCourseId(){return CourseId;}
    public void setCourseId(int CourseId){this.CourseId=CourseId;}
    public String getCourseName(){return CourseName;}
    public void setCourseName(String CourseName){this.CourseName=CourseName;}
    public String getTeacher(){return Teacher;}
    public void setTeacher(String Teacher){this.Teacher=Teacher;}
    public String getProperty(){return Property;}
    public void setProperty(String Property){this.Property=Property;}
    public String getScore(){return Score;}
    public void setScore(String Score){this.Score=Score;}
    public int getLiked(){return Liked;}
    public void setLiked(int Liked){this.Liked=Liked;}
    public int getDisliked(){return Disliked;}
    public void setDisliked(int Disliked){this.Disliked=Disliked;}

 

    public class CourseList{

        private List<CourseModel> courses;

        public List<CourseModel> getCourses() {
            return courses;
        }

        public void setCourses(List<CourseModel> courses) {
            this.courses = courses;
        }
    }

}

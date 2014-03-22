package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.List;
/**
 * 2014-3-18
 */

public class CourseCommentModel implements Serializable {
    private int Id;
    private int CourseId;
    private String UserName;
    private boolean hasHomework;
    private boolean isCheck;
    private String Exam;
    private String Comment;
    private String time;

    public class CourseCommentList {
        private CourseModel courseInfo;
        private List<CourseCommentModel> comments;
        private int sumPage;
        private int count;
        private String status;

        public CourseModel getCourseInfo() {
            return courseInfo;
        }

        public void setCourseInfo(CourseModel courseInfo) {
            this.courseInfo = courseInfo;
        }

        public List<CourseCommentModel> getComments() {
            return comments;
        }

        public void setComments(List<CourseCommentModel> comments) {
            this.comments = comments;
        }

        public int getSumPage() {
            return sumPage;
        }

        public void setSumPage(int sumPage) {
            this.sumPage = sumPage;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getCourseId() {
        return CourseId;
    }

    public void setCourseId(int CourseId) {
        this.CourseId = CourseId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public boolean getHasHomework() {
        return hasHomework;
    }

    public void setHasHomework(boolean hasHomework) {
        this.hasHomework = hasHomework;
    }

    public boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getExam() {
        return Exam;
    }

    public void setExam(String Exam) {
        this.Exam = Exam;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

package cn.scau.scautreasure.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User:  Special Leung
 * Date:  13-8-17
 * Time:  下午2:07
 * Mail:  specialcyci@gmail.com
 */

//        "学年" => "year",
//        "学期" => "team",
//        "课程代码" => "code",
//        "课程名称" => "name",
//        "课程性质" => "classify",
//        "课程归属" => "college_belong",
//        "学分" => "credit",
//        "绩点" => "grade_point",
//        "成绩" => "goal",
//        "辅修标记" => "flag_minor",
//        "重修成绩" => "goal_restudy",
//        "开课学院" => "college_hold",
//        "重修标记" => "flag_restudy"
//"平时成绩" => "goal_regular",
//        "期末成绩" => "goal_exam",
public class GoalModel implements Serializable {
    private String year;
    private String team;
    private String code;
    private String name;
    private String classify;
    private String college_belong;
    private String credit;
    private String grade_point;
    private String goal;
    private String flag_minor;
    private String goal_restudy;
    private String college_hold;
    private String flag_restudy;
    private String goal_regular;
    private String goal_exam;

    public String getGoal_regular() {
        return goal_regular;
    }

    public void setGoal_regular(String goal_regular) {
        this.goal_regular = goal_regular;
    }

    public String getGoal_exam() {
        return goal_exam;
    }

    public void setGoal_exam(String goal_exam) {
        this.goal_exam = goal_exam;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getCollege_belong() {
        return college_belong;
    }

    public void setCollege_belong(String college_belong) {
        this.college_belong = college_belong;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getGrade_point() {
        return grade_point;
    }

    public void setGrade_point(String grade_point) {
        this.grade_point = grade_point;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getFlag_minor() {
        return flag_minor;
    }

    public void setFlag_minor(String flag_minor) {
        this.flag_minor = flag_minor;
    }

    public String getGoal_restudy() {
        return goal_restudy;
    }

    public void setGoal_restudy(String goal_restudy) {
        this.goal_restudy = goal_restudy;
    }

    public String getCollege_hold() {
        return college_hold;
    }

    public void setCollege_hold(String college_hold) {
        this.college_hold = college_hold;
    }

    public String getFlag_restudy() {
        return flag_restudy;
    }

    public void setFlag_restudy(String flag_restudy) {
        this.flag_restudy = flag_restudy;
    }

    @Override
    public String toString() {
        return "GoalModel{" +
                "year='" + year + '\'' +
                ", team='" + team + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", classify='" + classify + '\'' +
                ", college_belong='" + college_belong + '\'' +
                ", credit='" + credit + '\'' +
                ", grade_point='" + grade_point + '\'' +
                ", goal='" + goal + '\'' +
                ", flag_minor='" + flag_minor + '\'' +
                ", goal_restudy='" + goal_restudy + '\'' +
                ", college_hold='" + college_hold + '\'' +
                ", flag_restudy='" + flag_restudy + '\'' +
                ", goal_regular='" + goal_regular + '\'' +
                ", goal_exam='" + goal_exam + '\'' +
                '}';
    }

    public class GoalList {

        private ArrayList<GoalModel> goals;

        public ArrayList<GoalModel> getGoals() {
            return goals;
        }

        public void setGoals(ArrayList<GoalModel> goals) {
            this.goals = goals;
        }
    }
}

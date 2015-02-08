package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.ClassRoomModel;
import cn.scau.scautreasure.model.ExamModel;
import cn.scau.scautreasure.model.GoalModel;
import cn.scau.scautreasure.model.ParamModel;
import cn.scau.scautreasure.model.PersonModel;
import cn.scau.scautreasure.model.PickClassModel;

/**
 * 教务系统Api;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午9:35
 * Mail:  specialcyci@gmail.com
 */

@Rest(rootUrl = "http://115.28.144.49/edusys/", converters = {GsonHttpMessageConverter.class})
public interface EdusysApi {

    @Get("login/{userName}/{passWord}/{server}")
    PersonModel login(String userName, String passWord, int server);//错误时候{"msg":"password_error"}

    /**
     * 更新课表
     *
     * @param userName
     * @param passWord
     *
     * @return
     */
    @Get("classtable/{userName}/{passWord}/1")
    ClassModel.ClassList getClassTable(String userName, String passWord);

    /**
     * 查成绩*
     *
     * @param userName
     * @param passWord
     * @param year
     * @param team
     *
     * @return
     */
    @Get("goal/{userName}/{passWord}/1/{year}/{team}")
    GoalModel.GoalList getGoal(String userName, String passWord, String year, String team);

    /**
     * 考试安排*
     * @param userName
     * @param passWord
     * @return
     */
    @Get("exam/{userName}/{passWord}/1")
    ExamModel.ExamList getExam(String userName, String passWord);

    @Get("pickclassinfo/{userName}/{passWord}/1")
    PickClassModel.PickClassList getPickClassInfo(String userName, String passWord);

    @Get("emptyclassroom/{userName}/{passWord}/1/{xq}/{jslb}/{ddlKsz}/{ddlJsz}/{sjd}/{xqj}/{dsz}")
    ClassRoomModel.ClassRoomList getEmptyClassRoom(String userName, String passWord,
                                                   String xq, String jslb, String ddlKsz, String ddlJsz,
                                                   String sjd, String xqj, String dsz);

    /**
     * 获取空教室参数*
     * @param userName
     * @param passWord
     * @param target
     * @return
     */
    @Get("params/{target}/{userName}/{passWord}/1")
    ParamModel.ParamList getParams(String userName, String passWord, String target);

}

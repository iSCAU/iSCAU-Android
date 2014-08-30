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
    PersonModel login(String userName, String passWord, int server);

    @Get("classtable/{userName}/{passWord}/{server}")
    ClassModel.ClassList getClassTable(String userName, String passWord, int server);

    @Get("goal/{userName}/{passWord}/{server}/{year}/{team}")
    GoalModel.GoalList getGoal(String userName, String passWord, int server, String year, String team);

    @Get("exam/{userName}/{passWord}/{server}")
    ExamModel.ExamList getExam(String userName, String passWord, int server);

    @Get("pickclassinfo/{userName}/{passWord}/{server}")
    PickClassModel.PickClassList getPickClassInfo(String userName, String passWord, int server);

    @Get("emptyclassroom/{userName}/{passWord}/{server}/{xq}/{jslb}/{ddlKsz}/{ddlJsz}/{sjd}/{xqj}/{dsz}")
    ClassRoomModel.ClassRoomList getEmptyClassRoom(String userName, String passWord, int server,
                                                   String xq, String jslb, String ddlKsz, String ddlJsz,
                                                   String sjd, String xqj, String dsz);

    @Get("params/{target}/{userName}/{passWord}/{server}")
    ParamModel.ParamList getParams(String userName, String passWord, int server, String target);

}

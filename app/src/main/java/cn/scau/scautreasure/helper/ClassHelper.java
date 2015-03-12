package cn.scau.scautreasure.helper;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.DateUtil;

/**
 * User: special
 * Date: 13-8-29
 * Time: 上午11:03
 * Mail: specialcyci@gmail.com
 */
@EBean
public class ClassHelper {

    @App
    AppContext app;

    @Bean
    DateUtil dateUtil;

    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @OrmLiteDao(helper = DatabaseHelper.class, model = ClassModel.class)
    RuntimeExceptionDao<ClassModel, Integer> classDao;

    /**
     * return all lesson in the database;
     *
     * @return
     */
    public List<ClassModel> getAllLesson() {
        return classDao.queryForAll();
    }

    /**
     * replace all lessons in the clsList to database;
     *
     * @param clsList
     */
    public void replaceAllLesson(List<ClassModel> clsList) {
        deleteAllLesson();
        for (ClassModel cls : clsList) classDao.create(cls);
    }

    /**
     * update the model in the database;
     *
     * @param model
     */
    public void createOrUpdateLesson(ClassModel model) {
        classDao.createOrUpdate(model);
    }

    /**
     * delete the model in the database;
     *
     * @param model
     */
    public void deleteLesson(ClassModel model) {
        classDao.delete(model);
    }

    /**
     * delete all lessons in the database;
     */
    public void deleteAllLesson() {
        classDao.delete(getAllLesson());
    }

    /**
     * load day's lesson no matter any condition
     *
     * @param wantDay
     *
     * @return
     */
    public List<ClassModel> getDayLesson(String wantDay) {
        PreparedQuery where = null;
        try {
            where = buildWhere(wantDay);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List clsList = classDao.query(where);
        return sortClassList(clsList);
    }

    public List<ClassModel> getDayLesson(int wantDay) {
        String day = dateUtil.numDayToChinese(wantDay);
        return getDayLesson(day);
    }

    /**
     * According to the start week and end week , odd and even week to load a day's lesson
     *
     * @param wantDay
     *
     * @return
     */
    public List<ClassModel> getDayLessonWithParams(String wantDay) {
        PreparedQuery where = null;
        try {
            where = buildWhere(wantDay, getSchoolWeek(), getSchoolWeekDsz());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List clsList = classDao.query(where);
        return sortClassList(clsList);
    }

    /**
     * 通过周来显示课表
     *
     * @param wantDay
     * @param week
     *
     * @return
     */
    public List<ClassModel> getDayLessonByWeek(String wantDay, int week) {
        PreparedQuery where = null;
        try {
            where = buildWhere(wantDay, week, week % 2 == 0 ? "双" : "单");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List clsList = classDao.query(where);
        return sortClassList(clsList);
    }

    public List<ClassModel> getDayLessonWithParams(int wantDay) {
        String day = dateUtil.numDayToChinese(wantDay);
        return getDayLessonWithParams(day);
    }

    /**
     * help to build a where to query in database;
     *
     * @return
     */
    private PreparedQuery buildWhere(String wantDay) throws SQLException {
        QueryBuilder<ClassModel, Integer> queryBuilder = classDao.queryBuilder();
        queryBuilder.clear();
        Where<ClassModel, Integer> where = queryBuilder.where();
        where.like("day", wantDay);
        return queryBuilder.prepare();
    }


    /**
     * help to build a where to query in database;;
     *
     * @return
     */
    private PreparedQuery buildWhere(String wantDay, int schoolWeek, String schoolWeekDsz) throws SQLException {
        QueryBuilder<ClassModel, Integer> queryBuilder = classDao.queryBuilder();
        queryBuilder.clear();
        Where<ClassModel, Integer> where = queryBuilder.where();
        where.and(
                where.like("day", wantDay),
                where.le("strWeek", schoolWeek),
                where.ge("endWeek", schoolWeek),
                where.or(
                        where.isNull("dsz"),
                        where.like("dsz", ""),
                        where.like("dsz", schoolWeekDsz)
                )
        );
        return queryBuilder.prepare();
    }

    /**
     * 获取当前第几周
     *
     * @return
     */
    public int getSchoolWeek() {
        String currentDate = dateUtil.getCurrentDateString();
        String termStartDate = config.termStartDate().get();

        return dateUtil.dateToSchoolWeek(currentDate, termStartDate);
    }

    public String getSchoolWeekDsz() {
        return dateUtil.judgeDsz(getSchoolWeek());
    }

    /**
     * sort the lesson according to nodes;
     *
     * @param classList
     */
    public List<ClassModel> sortClassList(List<ClassModel> classList) {
        Collections.sort(classList, new SortByNodes());
        return classList;
    }

    static class SortByNodes implements Comparator<ClassModel> {
        @Override
        public int compare(ClassModel arg0, ClassModel arg1) {

            if (getFirstNode(arg0) < getFirstNode(arg1))
                return -1;
            return 1;
        }

        private int getFirstNode(ClassModel model) {
            String[] nodes = model.getNode().split(",");
            return Integer.valueOf(nodes[0]);
        }
    }


}

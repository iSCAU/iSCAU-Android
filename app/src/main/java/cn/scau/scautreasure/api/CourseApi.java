package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;

import cn.scau.scautreasure.model.CourseCommentModel;
import cn.scau.scautreasure.model.CourseModel;
/**
 * 2014-3-19
 */

@Rest(rootUrl = "http://76.191.112.146/course_comments/index.php?s=Api/", converters = {GsonHttpMessageConverter.class})
public interface CourseApi {
    @Get("search/keyword/{course}")
    CourseModel.CourseList searchCourse(String course) throws RestClientException;

    @Get("comment/courseId/{courseId}/page/{page}")
    CourseCommentModel.CourseCommentList getComments(int courseId, int page) throws RestClientException;
}

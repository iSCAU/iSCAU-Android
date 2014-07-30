package cn.scau.scautreasure.api;

/**
 * Created by stcdasqy on 14-3-14.


@Rest(rootUrl = "http://76.191.112.146/course_comments/index.php?s=Api/",
        converters = {  FormHttpMessageConverter.class,
                StringHttpMessageConverter.class} )
public interface CourseApiPost {
    @Post("addComment")
    StatusModel addComment(MultiValueMap params) throws RestClientException;

    @Post("likedCourse")
    StatusModel likedCourse(HashMap params) throws RestClientException;

    @Post("dislikedCourse")
    StatusModel dislikedCourse(HashMap params) throws RestClientException;
}

*/
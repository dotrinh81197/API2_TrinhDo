package api;

import common.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.RestAssuredConfiguration;

import static io.restassured.RestAssured.given;

public class ClassRoomAPI extends RestAssuredConfiguration {

    private String BASE_PATH = "/v1/courses";
    private String GET_COURSE = "/{id}";
    private String CREATE_TOPIC = "/{courseId}/topics";
    private String GET_TOPIC = "/{courseId}/topics/{id}";
    private String CREATE_COURSEWORK = "/{courseId}/courseWork";
    private String GET_COURSEWORK = "/{courseId}/courseWork/{id}";

    public RequestSpecification classroomAPI(String access_token) {
        return given()
                .log()
                .all()
                .baseUri(Constants.BASE_URI)
                .basePath(BASE_PATH)
                .auth()
                .oauth2(access_token)
                .header("Content-Type", "application/json");
    }


    public Response getCourse(String access_token, String courseId) {
        Response response = classroomAPI(access_token).get(GET_COURSE, courseId);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response getTopic(String access_token, String courseId, String topicId) {
        Response response = classroomAPI(access_token).get(GET_TOPIC, courseId, topicId);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response getCourseWork(String access_token, String courseId, String courseWorkId) {
        Response response = classroomAPI(access_token).get(GET_COURSEWORK, courseId, courseWorkId);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response createCourse(String access_token, Object body) {
        Response response = classroomAPI(access_token).body(body.toString()).post();
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response createTopic(String access_token,String courseId, Object body) {
        Response response = classroomAPI(access_token).body(body.toString()).post(CREATE_TOPIC, courseId);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response createCourseWork(String access_token, String courseId, Object body) {
        Response response = classroomAPI(access_token).body(body.toString()).post(CREATE_COURSEWORK, courseId);
        System.out.println(response.getBody().asString());
        return response;
    }
}

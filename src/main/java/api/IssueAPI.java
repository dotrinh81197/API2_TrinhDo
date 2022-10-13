package api;

import common.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.RestAssuredConfiguration;

import static io.restassured.RestAssured.given;

public class IssueAPI extends RestAssuredConfiguration {

    private String GET_ISSUE = "/{issueIdOrKey}";
    private String ADD_COMMENT = "{issueIdOrKey}/comment";
    private String GET_COMMENT = "{issueIdOrKey}/comment";
    private String GET_COMMENT_ID = "{issueIdOrKey}/comment/{id}";
    private String UPDATE_COMMENT = "{issueIdOrKey}/comment/{id}";
    private String DELETE_COMMENT = "/{issueIdOrKey}/comment/{id}";
    private String DELETE_ISSUE = "/{issueIdOrKey}";
    private String BASE_PATH = "/rest/api/3/issue";

    public RequestSpecification issueAPI(String access_token) {
        return given()
                .log()
                .all()
                .baseUri(Constants.BASE_URI)
                .basePath(BASE_PATH)
                .auth()
                .oauth2(access_token)
                .header("Content-Type", "application/json");
    }


    public Response createIssue(String access_token, Object body) {
        Response response = issueAPI(access_token).body(body.toString()).post();
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response getIssue(String access_token, String issueIdOrKey) {
        Response response = issueAPI(access_token).get(GET_ISSUE, issueIdOrKey);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response addComment(String access_token, String issueIdOrKey, Object body) {
        Response response = issueAPI(access_token).body(body.toString()).post(ADD_COMMENT, issueIdOrKey);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response updateComment(String access_token, String issueIdOrKey, String commentId, Object body) {
        Response response = issueAPI(access_token).body(body.toString()).put(UPDATE_COMMENT, issueIdOrKey, commentId);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response getComment(String access_token, String issueIdOrKey) {
        Response response = issueAPI(access_token).get(GET_COMMENT, issueIdOrKey);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response getCommentId(String access_token, String issueIdOrKey, String commentId) {
        Response response = issueAPI(access_token).get(GET_COMMENT_ID, issueIdOrKey, commentId);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response deleteIssue(String access_token, String issueIdOrKey) {
        Response response = issueAPI(access_token).delete(DELETE_ISSUE, issueIdOrKey);
        System.out.println(response.getBody().asString());
        return response;
    }

    public Response deleteComment(String access_token, String issueIdOrKey) {
        Response response = issueAPI(access_token).delete(DELETE_COMMENT, issueIdOrKey);
        System.out.println(response.getBody().asString());
        return response;
    }

}

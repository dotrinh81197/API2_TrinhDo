package api;

import common.Constants;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.RestAssuredConfiguration;

import static io.restassured.RestAssured.given;

public class IssueAPI extends RestAssuredConfiguration {

    private String GET_ISSUE = "/{issueIdOrKey}";
    private String ADD_COMMENT = "{issueIdOrKey}/comment";
    private String DELETE_COMMENT = "/{issueIdOrKey}/comment/{id}";
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

    public Response addComment(String access_token, String issueIdOrKey, Object body) {
        Response response = issueAPI(access_token).body(body.toString()).post(ADD_COMMENT, issueIdOrKey);
        System.out.println(response.getBody().asString());
        return response;
    }


}

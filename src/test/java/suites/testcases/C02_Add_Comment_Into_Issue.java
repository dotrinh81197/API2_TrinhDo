package suites.testcases;

import api.IssueAPI;
import testbase.TestBase;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;

import java.util.Hashtable;

public class C02_Add_Comment_Into_Issue extends TestBase {
    private IssueAPI issueAPI = new IssueAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();

    @Test(dataProvider = "getDataForTest", description = "Verify add comment into issue API")
    public void TC01_Add_Comment_Successfully(Hashtable<String, Object> data) {
        int statusCode = ((Double) data.get("statusCode")).intValue();

        logReport.logStep("Step 1: Create an issue successfully");
        String accessToken = restAssuredConfiguration.getAuth2Token();
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("PayLoadCreateIssue"));
        Response response = issueAPI.createIssue(accessToken, payload);
        JSONObject responseBody = new JSONObject(response.body().asString());
        //getIssueId
        String issueId = responseBody.getString("id");

        stepDescription = "Step 2: Add a comment into issue";
        JSONObject payloadAddComment = utils.JsonConverter.toJsonObject(data.get("PayLoadAddComment"));
        response = issueAPI.addComment(accessToken, issueId, payloadAddComment);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify response contains id of comment";
        responseBody = new JSONObject(response.body().asString());
        String commentPostId = responseBody.getString("id");
        assertion.assertNotNull(commentPostId, stepDescription);

        stepDescription = "Step 5: Verify add comment successfully";
        stepDescription = "Step 5.1: Get comment into issue";
        response = issueAPI.getComment(accessToken, issueId);
        responseBody = new JSONObject(response.body().asString());
        JSONArray comment = responseBody.getJSONArray("comments");
        JSONObject commentObject = (JSONObject) comment.get(0);

        stepDescription = "Step 5.2: Verify comment id matching";
        String commentGetId = commentObject.getString("id");
        assertion.assertEquals(commentPostId, commentGetId, stepDescription);
        JSONObject commentBody = commentObject.getJSONObject("body");

        stepDescription = "Step 5.3: Verify comment text matching";
        //Get comment text in response
        JSONArray content = commentBody.getJSONArray("content");
        JSONObject contentActual = content.getJSONObject(0);
        String contentActualText = contentActual.getJSONArray("content").getJSONObject(0).getString("text");
        //Get comment text in payload
        JSONObject contentExpectedObject = (JSONObject) payloadAddComment.get("body");
        JSONObject contentExpectedObject2 = (JSONObject) contentExpectedObject.getJSONArray("content").get(0);
        JSONArray contentExpectedArray = contentExpectedObject2.getJSONArray("content");
        JSONObject contentExpectedText = (JSONObject) contentExpectedArray.get(0);
        String contentExpected = contentExpectedText.getString("text");
        assertion.assertEquals(contentActualText, contentExpected, stepDescription);

    }

    @Test(dataProvider = "getDataForTest", description = "Verify add comment with empty text")
    public void TC02_Add_Comment_With_Empty_Text(Hashtable<String, Object> data) {
        int statusCode = ((Double) data.get("BadRequestStatus")).intValue();

        logReport.logStep("Step 1: Create an issue successfully");
        String accessToken = restAssuredConfiguration.getAuth2Token();
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("PayLoadCreateIssue"));
        Response response = issueAPI.createIssue(accessToken, payload);
        JSONObject responseBody = new JSONObject(response.body().asString());
        //getIssueId
        String issueId = responseBody.getString("id");

        stepDescription = "Step 2: Add a comment into issue";
        JSONObject payloadAddComment = utils.JsonConverter.toJsonObject(data.get("PayLoadAddCommentEmptyText"));
        Response AddCommentResponse = issueAPI.addComment(accessToken, issueId, payloadAddComment);


        stepDescription = "Step 3: Verify status code return to" + statusCode;
        assertion.assertEquals(AddCommentResponse.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify error comment response ";
        responseBody = new JSONObject(AddCommentResponse.body().asString());
        JSONObject errors = responseBody.getJSONObject("errors");
        String errorSummary = errors.getString("comment");
        System.out.println(errorSummary);
        assertion.assertEquals(errorSummary, (String) data.get("ErrorsComment"), "Errors Comment display");

    }


}

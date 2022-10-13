package suites.testcases;

import api.IssueAPI;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class C04_Update_Comment extends TestBase {
    private IssueAPI issueAPI = new IssueAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();

    @Test(dataProvider = "getDataForTest", description = "Verify update comment API")
    public void TC01_Update_Comment_Successfully(Hashtable<String, Object> data) {
        int statusCode = ((Double) data.get("SuccessStatusCode")).intValue();

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
        //getCommentId
        responseBody = new JSONObject(response.body().asString());
        String commentId = responseBody.getString("id");

        stepDescription = "Step 3: Call API update comment";
        JSONObject payloadUpdateComment = utils.JsonConverter.toJsonObject(data.get("PayLoadUpdateComment"));
        response = issueAPI.updateComment(accessToken, issueId, commentId, payloadUpdateComment);

        stepDescription = "Step 4: Verify status response return" + statusCode;
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 5: Verify response Id match with comment id";
        responseBody = new JSONObject(response.body().asString());
        assertion.assertEquals(responseBody.getString("id"), commentId, stepDescription);

        stepDescription = "Step 6: Verify response create time and update time are different";
        assertion.assertNotEquals(responseBody.getString("created"), responseBody.getString("updated"), stepDescription);

        stepDescription = "Step 7: Verify comment text is matching";
        //get comment text in response
        JSONObject body = responseBody.getJSONObject("body");
        System.out.println("````````````````````````````");
        JSONArray content = body.getJSONArray("content");
        JSONObject contentObject = content.getJSONObject(0);
        JSONObject contentArray = contentObject.getJSONArray("content").getJSONObject(0);
        String contentActualText = contentArray.getString("text");
        System.out.println(contentActualText);

        //get comment text in payload updated comment
        JSONObject contentPayloadObject = (JSONObject) payloadUpdateComment.get("body");
        JSONObject contentPayloadObject2 = (JSONObject) contentPayloadObject.getJSONArray("content").get(0);
        JSONArray contentExpectedArray = contentPayloadObject2.getJSONArray("content");
        JSONObject contentExpectedText = (JSONObject) contentExpectedArray.get(0);
        String contentExpected = contentExpectedText.getString("text");

        assertion.assertEquals(contentActualText, contentExpected, stepDescription);

        //get comment id after updated
        stepDescription = "Step 8: Get comment after updated ";
        response = issueAPI.getCommentId(accessToken, issueId, commentId);

        stepDescription = "Step 9: Verify comment after updated match with expected";
        responseBody = new JSONObject(response.body().asString());
        body = responseBody.getJSONObject("body");
        content = body.getJSONArray("content");
        contentObject = content.getJSONObject(0);
        contentArray = contentObject.getJSONArray("content").getJSONObject(0);
        contentActualText = contentArray.getString("text");
        assertion.assertEquals(contentActualText, contentExpected, stepDescription);

    }
}

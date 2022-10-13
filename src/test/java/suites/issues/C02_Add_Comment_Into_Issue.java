package suites.issues;

import api.IssueAPI;
import base.TestBase;
import io.restassured.response.Response;
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
    public void TC01(Hashtable<String, Object> data) {
        int statusCode = ((Double) data.get("statusCode")).intValue();

        logReport.logStep("Step 1: Create an issue successfully");
        String accessToken = restAssuredConfiguration.getAuth2Token();
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("PayLoadCreateIssue"));
        Response response = issueAPI.createIssue(accessToken, payload);
        JSONObject responseBody = new JSONObject(response.body().asString());
        //getIssueId
        String issueId = responseBody.getString("id");

        stepDescription = "Step 2: Add a comment into issue";
        payload = utils.JsonConverter.toJsonObject(data.get("PayLoadAddComment"));
        response = issueAPI.addComment(accessToken, issueId, payload);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify response contains text of comment";
        responseBody = new JSONObject(response.body().asString());
//        assertion.assertNotNull(responseBody.getString("body"), stepDescription);


    }


}

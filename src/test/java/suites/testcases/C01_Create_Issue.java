package suites.testcases;

import api.IssueAPI;
import testbase.TestBase;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;

import java.util.Hashtable;

public class C01_Create_Issue extends TestBase {
    private IssueAPI issueAPI = new IssueAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();

    @Test(dataProvider = "getDataForTest", description = "Verify create issue API successfully with valid data")
    public void TC01_Create_Issue_Successfully(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("SuccessStatusCode")).intValue();

        logReport.logStep("Step 1: Get access token");
        String accessToken = restAssuredConfiguration.getAuth2Token();

        logReport.logStep("Step 2: Call create issue API");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("body"));
        Response response = issueAPI.createIssue(accessToken, payload);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify response contains key and id of issue";
        JSONObject responseBody = new JSONObject(response.body().asString());
        String issueKey = responseBody.getString("key");
        String issueId = responseBody.getString("id");
        assertion.assertNotNull(issueKey, stepDescription);
        assertion.assertNotNull(issueId, stepDescription);

        stepDescription = "Step 5: Verify issue create successfully";
        response = issueAPI.getIssue(accessToken, issueKey);
        assertion.assertEquals(response.statusCode(), "200", stepDescription);
        responseBody = new JSONObject(response.body().asString());
        assertion.assertEquals(responseBody.getString("key"), issueKey, "Issue's key is matched");
        assertion.assertEquals(responseBody.getString("id"), issueId, "Issue's id is matched");

    }

    @Test(dataProvider = "getDataForTest", description = "Verify create issue API with empty summary")
    public void TC02_Create_Issue_Empty_Summary(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("BadRequestStatus")).intValue();

        logReport.logStep("Step 1: Get access token");
        String accessToken = restAssuredConfiguration.getAuth2Token();

        logReport.logStep("Step 2: Call create issue API");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("bodyEmptySummary"));
        Response response = issueAPI.createIssue(accessToken, payload);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify error summary response ";
        JSONObject responseBody = new JSONObject(response.body().asString());
        JSONObject errors = responseBody.getJSONObject("errors");
        String errorSummary = errors.getString("summary");
        System.out.println(errorSummary);
        assertion.assertEquals(errorSummary, (String) data.get("ErrorsSummary"), "Errors summary display");

    }

}

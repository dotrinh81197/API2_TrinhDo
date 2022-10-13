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

public class C03_Delete_Issue extends TestBase {
    private IssueAPI issueAPI = new IssueAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();

    @Test(dataProvider = "getDataForTest", description = "Verify delete issue successfully")
    public void TC01_Delete_Issue_Successfully(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("SuccessStatusCode")).intValue();

        logReport.logStep("Step 1: Get access token");
        String accessToken = restAssuredConfiguration.getAuth2Token();

        logReport.logStep("Step 2: Call create issue API");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("PayLoadCreateIssue"));
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

        stepDescription = "Step 6: Call API Delete Issue";
        response = issueAPI.deleteIssue(accessToken, issueKey);
        assertion.assertEquals(response.statusCode(), "204", stepDescription);

        stepDescription = "Step 7: Verify delete issue successfully";
        response = issueAPI.getIssue(accessToken, issueKey);
        assertion.assertEquals(response.statusCode(), "404", stepDescription);

        stepDescription = "Step 8: Verify error massages response ";
        responseBody = new JSONObject(response.body().asString());
        JSONArray errors = responseBody.getJSONArray("errorMessages");
        String errorMessage = errors.getString(0);
        System.out.println(errorMessage);
        assertion.assertEquals(errorMessage, (String) data.get("ErrorsSummary"), "Errors summary display");

    }
}

package suites.issues;

import api.IssueAPI;
import base.TestBase;
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

    @Test(dataProvider = "getDataForTest", description = "Verify create issue API")
    public void TC01(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("statusCode")).intValue();

        logReport.logStep("Step 1: Get access token");
        String accessToken = restAssuredConfiguration.getAuth2Token();

        logReport.logStep("Step 2: Call create issue API");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("body"));
        Response response = issueAPI.createIssue(accessToken, payload);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify response contains key of issue";
        JSONObject responseBody = new JSONObject(response.body().asString());
        assertion.assertNotNull(responseBody.getString("key"), stepDescription);

    }

}

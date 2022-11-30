package suites.testcases;

import api.ClassRoomAPI;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class TC04_Create_Topic_With_Empty_Name extends TestBase {

    private ClassRoomAPI classRoomAPI = new ClassRoomAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();


    @Test(dataProvider = "getDataForTest", description = "Verify Create a Topics Empty Name")
    public void TC04_Create_Topic_With_Empty_Name(Hashtable<String, Object> data) {

        int badStatusCode = ((Double) data.get("BadRequestStatus")).intValue();

        logReport.logStep("Step1: Get Oauth2 token");
        String accessToken = restAssuredConfiguration.getOAuth2Token();

        logReport.logStep("Step2: Call API create a valid course ");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateCourse"));
        Response response = classRoomAPI.createCourse(accessToken, payload);
        JSONObject responseBody = new JSONObject(response.body().asString());
        String courseId = responseBody.getString("id");

        logReport.logStep("Step2: Call API create a topic empty name");
         payload = utils.JsonConverter.toJsonObject(data.get("bodyTopicEmptyName"));
         response = classRoomAPI.createTopic(accessToken, courseId, payload);;

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), badStatusCode, stepDescription);

        stepDescription = "Step 4: Verify error summary response ";
         responseBody = new JSONObject(response.body().asString());
        JSONObject errors = responseBody.getJSONObject("error");
        String errorMessage = errors.getString("message");
        String errorStatus = errors.getString("status");
        System.out.println(errorMessage);
        System.out.println(errorStatus);
        assertion.assertEquals(errorMessage, (String) data.get("ErrorsMessage"), "Errors summary display");
        assertion.assertEquals(errorStatus, (String) data.get("ErrorsStatus"), "Errors summary display");

    }
}

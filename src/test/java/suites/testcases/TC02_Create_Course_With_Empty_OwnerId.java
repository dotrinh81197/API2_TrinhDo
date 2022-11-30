package suites.testcases;

import api.ClassRoomAPI;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class TC02_Create_Course_With_Empty_OwnerId extends TestBase {

    private ClassRoomAPI classRoomAPI = new ClassRoomAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();


    @Test(dataProvider = "getDataForTest", description = "Verify Create a Course Empty OwnerId")
    public void TC02_Create_Course_With_Empty_OwnerId(Hashtable<String, Object> data) {

        int badStatusCode = ((Double) data.get("BadRequestStatus")).intValue();

        logReport.logStep("Step1: Get Oauth2 token");
        String accessToken = restAssuredConfiguration.getOAuth2Token();

        logReport.logStep("Step2: Call API create a course with empty ownerId");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("bodyEmptyOwnerId"));
        Response response = classRoomAPI.createCourse(accessToken, payload);
        System.out.println(response);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), badStatusCode, stepDescription);

        stepDescription = "Step 4: Verify error summary response ";
        JSONObject responseBody = new JSONObject(response.body().asString());
        JSONObject errors = responseBody.getJSONObject("error");
        String errorMessage = errors.getString("message");
        String errorStatus = errors.getString("status");
        System.out.println(errorMessage);
        System.out.println(errorStatus);
        assertion.assertEquals(errorMessage, (String) data.get("ErrorsMessage"), "Errors summary display");
        assertion.assertEquals(errorStatus, (String) data.get("ErrorsStatus"), "Errors summary display");

    }

}



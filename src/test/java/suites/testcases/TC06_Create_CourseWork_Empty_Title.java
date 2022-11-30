package suites.testcases;

import api.ClassRoomAPI;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class TC06_Create_CourseWork_Empty_Title extends TestBase {

    private ClassRoomAPI classRoomAPI = new ClassRoomAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();


    @Test(dataProvider = "getDataForTest", description = "Verify Create a CourseWork Empty Title")
    public void TC06_Create_CourseWork_Empty_Title(Hashtable<String, Object> data) {

        int badStatusCode = ((Double) data.get("BadRequestStatus")).intValue();

        logReport.logStep("Step1: Get Oauth2 token");
        String accessToken = restAssuredConfiguration.getOAuth2Token();

        logReport.logStep("Step2: Call API create a valid course");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateCourse"));
        Response response = classRoomAPI.createCourse(accessToken, payload);
        JSONObject responseBody = new JSONObject(response.body().asString());
        String courseId = responseBody.getString("id");

        stepDescription = "Step 3: Call API create a topic";
        payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateTopic"));
        response = classRoomAPI.createTopic(accessToken, courseId, payload);
        responseBody = new JSONObject(response.body().asString());
        String topicId = responseBody.getString("topicId");

        stepDescription = "Step 4: Call API create a Course Work empty Title";
        payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateCourseWorkEmptyTitle"));
        payload.put("topicId", topicId);
        response = classRoomAPI.createCourseWork(accessToken, courseId, payload);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), badStatusCode, stepDescription);

        stepDescription = "Step 4: Verify error response ";
        responseBody = new JSONObject(response.body().asString());
        JSONObject errors = responseBody.getJSONObject("error");
        String errorMessage = errors.getString("message");
        String errorStatus = errors.getString("status");
        System.out.println(errorMessage);
        System.out.println(errorStatus);
        assertion.assertEquals(errorMessage, (String) data.get("ErrorsMessage"), "ErrorsMessage display");
        assertion.assertEquals(errorStatus, (String) data.get("ErrorsStatus"), "ErrorsStatus display");

    }
}

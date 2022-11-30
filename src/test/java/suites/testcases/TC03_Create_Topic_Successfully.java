package suites.testcases;

import api.ClassRoomAPI;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class TC03_Create_Topic_Successfully extends TestBase {

    private ClassRoomAPI classRoomAPI = new ClassRoomAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();


    @Test(dataProvider = "getDataForTest", description = "Verify Create a Topic Successfully")
    public void TC03_Create_Topic_Successfully(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("SuccessStatusCode")).intValue();

        logReport.logStep("Step1: Get Oauth2 token");
        String accessToken = restAssuredConfiguration.getOAuth2Token();

        logReport.logStep("Step2: Call API create a valid topic");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateCourse"));
        Response response = classRoomAPI.createCourse(accessToken, payload);
        JSONObject responseBody = new JSONObject(response.body().asString());
        String courseId = responseBody.getString("id");

        stepDescription = "Step 3: Call API create a topic";
        payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateTopic"));
        response = classRoomAPI.createTopic(accessToken, courseId, payload);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify course create successfully";
        responseBody = new JSONObject(response.body().asString());
        String topicId = responseBody.getString("topicId");
        String name = responseBody.getString("name");

        response = classRoomAPI.getTopic(accessToken, courseId, topicId);
        assertion.assertEquals(response.statusCode(), "200", stepDescription);
        responseBody = new JSONObject(response.body().asString());
        assertion.assertEquals(responseBody.getString("courseId"), courseId, "course's id is matched");
        assertion.assertEquals(responseBody.getString("name"), name, "name topic is matched");
        assertion.assertNotNull(responseBody.getString("topicId"), "topicId not null");
        assertion.assertNotNull(responseBody.getString("updateTime"), "update time not null");

    }

}

package suites.testcases;

import api.ClassRoomAPI;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class TC05_Create_CourseWork_Successfully extends TestBase {

    private ClassRoomAPI classRoomAPI = new ClassRoomAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();


    @Test(dataProvider = "getDataForTest", description = "Verify Create a Course Work Successfully")
    public void TC05_Create_CourseWork_Successfully(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("SuccessStatusCode")).intValue();

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

        stepDescription = "Step 4: Call API create a Course Work";
        payload = utils.JsonConverter.toJsonObject(data.get("bodyCreateCourseWork"));
        payload.put("topicId", topicId);
        response = classRoomAPI.createCourseWork(accessToken, courseId, payload);

        stepDescription = "Step 5: Verify status code";
        assertion.assertEquals(response.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 6: Verify course work create successfully";
        responseBody = new JSONObject(response.body().asString());
        courseId = responseBody.getString("courseId");
        String title = responseBody.getString("title");
        String description = responseBody.getString("description");
        String workType = responseBody.getString("workType");
        topicId = responseBody.getString("topicId");
        String courseWorkId = responseBody.getString("id");

        response = classRoomAPI.getCourseWork(accessToken, courseId, courseWorkId);
        assertion.assertEquals(response.statusCode(), "200", stepDescription);
        responseBody = new JSONObject(response.body().asString());
        assertion.assertEquals(responseBody.getString("courseId"), courseId, "courseId is matched");
        assertion.assertNotNull(responseBody.getString("id"), "id is not null");
        assertion.assertEquals(responseBody.getString("title"), title, "title is matched");
        assertion.assertEquals(responseBody.getString("description"), description, "description is matched");
        assertion.assertEquals(responseBody.getString("workType"), workType, "workType is matched");
        assertion.assertEquals(responseBody.getString("topicId"), topicId, "topicId is matched");
        assertion.assertNotNull(responseBody.getString("assigneeMode"), "assigneeMode time not null");

    }
}

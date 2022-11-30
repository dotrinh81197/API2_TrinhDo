package suites.testcases;

import api.ClassRoomAPI;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;
import reportUtils.Assertion;
import reportUtils.LogReport;
import testbase.TestBase;

import java.util.Hashtable;

public class TC01_Create_Course_Successfully extends TestBase {
    private ClassRoomAPI classRoomAPI = new ClassRoomAPI();
    private Assertion assertion = new Assertion();
    LogReport logReport = new LogReport();
    private utils.RestAssuredConfiguration restAssuredConfiguration = new utils.RestAssuredConfiguration();


    @Test(dataProvider = "getDataForTest", description = "Verify Create a Course Successfully")
    public void TC01_Create_Course_Successfully(Hashtable<String, Object> data) {

        int statusCode = ((Double) data.get("SuccessStatusCode")).intValue();

        logReport.logStep("Step1: Get Oauth2 token");
        String accessToken = restAssuredConfiguration.getOAuth2Token();

        logReport.logStep("Step2: Call API create a course");
        JSONObject payload = utils.JsonConverter.toJsonObject(data.get("body"));
        Response createResponse = classRoomAPI.createCourse(accessToken, payload);
        System.out.println(createResponse);

        stepDescription = "Step 3: Verify status code";
        assertion.assertEquals(createResponse.statusCode(), statusCode, stepDescription);

        stepDescription = "Step 4: Verify course create successfully";
        JSONObject createResponseBody = new JSONObject(createResponse.body().asString());
        String courseId = createResponseBody.getString("id");
        String name = createResponseBody.getString("name");
        String section = createResponseBody.getString("section");
        String descriptionHeading = createResponseBody.getString("descriptionHeading");
        String description = createResponseBody.getString("description");
        String room = createResponseBody.getString("room");
        String ownerId = createResponseBody.getString("ownerId");
        String courseState = createResponseBody.getString("courseState");
        assertion.assertNotNull(courseId, stepDescription);
        Response response = classRoomAPI.getCourse(accessToken, courseId);
        assertion.assertEquals(response.statusCode(), "200", stepDescription);
        JSONObject responseBody = new JSONObject(response.body().asString());
        assertion.assertEquals(responseBody.getString("id"), courseId, "course's id is matched");
        assertion.assertEquals(responseBody.getString("name"), name, "name is matched");
        assertion.assertEquals(responseBody.getString("section"), section, "section is matched");
        assertion.assertEquals(responseBody.getString("descriptionHeading"), descriptionHeading, "descriptionHeading id is matched");
        assertion.assertEquals(responseBody.getString("description"), description, "description is matched");
        assertion.assertEquals(responseBody.getString("room"), room, "room is matched");
        assertion.assertEquals(responseBody.getString("courseState"), courseState, "courseState  is matched");
    }


}

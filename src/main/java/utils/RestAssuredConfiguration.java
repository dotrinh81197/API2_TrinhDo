package utils;

import data.Key;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static utils.PropertiesHelper.getProperty;

public class RestAssuredConfiguration {
    public String getAuth2Token() {
        Response response = given()
                .log().all()
                .baseUri(getProperty(Key.AUTHORIZATION_URL))
                .formParam("grant_type","client_credentials")
                .formParam("client_id", getProperty(Key.CLIENT_ID))
                .formParam("client_secret", getProperty(Key.CLIENT_SECRET))
                .when()
                .post();

        return response.jsonPath().get("access_token");
    }

}

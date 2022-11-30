package utils;

import data.Key;
import io.restassured.response.Response;

import static common.Constants.REFRESH_TOKEN;
import static io.restassured.RestAssured.given;
import static utils.PropertiesHelper.getProperty;

public class RestAssuredConfiguration {
    public static String getRefreshToken() {
        Response response = given()
                .log().all()
                .baseUri(getProperty(Key.AUTHORIZATION_URL))
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")
                .queryParam("client_id", getProperty(Key.CLIENT_ID))
                .queryParam("client_secret", getProperty(Key.CLIENT_SECRET))
                .queryParam("code", getProperty(Key.CODE))
                .when()
                .post();

        return response.jsonPath().get("refresh_token");
    }

    public String getOAuth2Token() {
             Response response = given()
                .log().all()
                .baseUri(getProperty(Key.AUTHORIZATION_URL))
                .formParam("grant_type", "refresh_token")
                .formParam("client_id", getProperty(Key.CLIENT_ID))
                .formParam("client_secret", getProperty(Key.CLIENT_SECRET))
                .formParam("refresh_token", REFRESH_TOKEN)
                .when()
                .post();

        return response.jsonPath().get("access_token");
    }

}

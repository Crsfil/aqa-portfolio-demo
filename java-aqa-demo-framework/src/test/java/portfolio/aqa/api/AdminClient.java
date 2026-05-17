package portfolio.aqa.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AdminClient {
    @Step("Get admin users")
    public Response getUsers(String token) {
        return given()
            .auth().oauth2(token)
        .when()
            .get("/api/admin/users")
        .then()
            .extract()
            .response();
    }
}

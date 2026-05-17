package portfolio.aqa.api;

import io.qameta.allure.Step;
import portfolio.aqa.model.LoginRequest;
import portfolio.aqa.model.LoginResponse;

import static io.restassured.RestAssured.given;

public class AuthClient {
    @Step("Login as {request.username}")
    public LoginResponse login(LoginRequest request) {
        return given()
            .contentType("application/json")
            .body(request)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract()
            .as(LoginResponse.class);
    }

    @Step("Try to login as {request.username}")
    public int loginAndReturnStatus(LoginRequest request) {
        return given()
            .contentType("application/json")
            .body(request)
        .when()
            .post("/api/auth/login")
        .then()
            .extract()
            .statusCode();
    }
}

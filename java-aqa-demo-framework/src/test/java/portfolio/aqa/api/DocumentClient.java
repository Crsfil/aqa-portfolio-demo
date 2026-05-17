package portfolio.aqa.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import portfolio.aqa.model.CreateDocumentRequest;
import portfolio.aqa.model.DocumentDto;

import static io.restassured.RestAssured.given;

public class DocumentClient {
    @Step("Create document")
    public DocumentDto createDocument(String token, CreateDocumentRequest request) {
        return given()
            .auth().oauth2(token)
            .contentType("application/json")
            .body(request)
        .when()
            .post("/api/documents")
        .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getObject("document", DocumentDto.class);
    }

    @Step("Try to create document")
    public Response tryCreateDocument(String token, CreateDocumentRequest request) {
        return given()
            .auth().oauth2(token)
            .contentType("application/json")
            .body(request)
        .when()
            .post("/api/documents")
        .then()
            .extract()
            .response();
    }
}

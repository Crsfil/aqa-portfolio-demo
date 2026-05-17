package portfolio.aqa.api;

import io.qameta.allure.Step;
import portfolio.aqa.model.ProjectDto;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ProjectClient {
    @Step("Get projects")
    public List<ProjectDto> getProjects(String token) {
        Map<String, List<ProjectDto>> response = given()
            .auth().oauth2(token)
        .when()
            .get("/api/projects")
        .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList("projects", ProjectDto.class);
    }
}

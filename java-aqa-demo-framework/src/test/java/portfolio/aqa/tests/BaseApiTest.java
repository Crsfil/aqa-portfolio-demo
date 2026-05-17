package portfolio.aqa.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import portfolio.aqa.config.TestConfig;

public abstract class BaseApiTest {
    @BeforeAll
    static void configureApiTests() {
        RestAssured.baseURI = TestConfig.baseUrl();
        RestAssured.filters(new AllureRestAssured());
    }
}

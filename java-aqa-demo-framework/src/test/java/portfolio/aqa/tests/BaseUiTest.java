package portfolio.aqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import portfolio.aqa.config.TestConfig;

public abstract class BaseUiTest {
    @BeforeAll
    static void configureUiTests() {
        Configuration.baseUrl = TestConfig.baseUrl();
        Configuration.browser = TestConfig.browser();
        Configuration.headless = TestConfig.headless();
        Configuration.timeout = 10_000;
        Configuration.browserSize = "1440x1000";

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
            .screenshots(true)
            .savePageSource(true));
    }
}

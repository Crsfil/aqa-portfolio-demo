package portfolio.aqa.tests.ui;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import portfolio.aqa.pages.LoginPage;
import portfolio.aqa.tests.BaseUiTest;

@Tag("ui")
class LoginUiTest extends BaseUiTest {
    private final LoginPage loginPage = new LoginPage();

    @Test
    @Tag("smoke")
    void managerCanLoginAndSeeProjects() {
        loginPage.openPage()
            .loginAs("manager", "manager123")
            .shouldBeOpened()
            .shouldHaveCurrentUserRole("MANAGER")
            .shouldHaveProjects();
    }

    @Test
    @Tag("regression")
    void invalidCredentialsShowLoginError() {
        loginPage.openPage()
            .submitInvalidCredentials("manager", "wrong-password")
            .shouldShowLoginError("Invalid username or password");
    }
}

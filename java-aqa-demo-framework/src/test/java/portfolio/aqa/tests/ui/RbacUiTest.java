package portfolio.aqa.tests.ui;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import portfolio.aqa.pages.LoginPage;
import portfolio.aqa.tests.BaseUiTest;

@Tag("ui")
class RbacUiTest extends BaseUiTest {
    private final LoginPage loginPage = new LoginPage();

    @Test
    @Tag("regression")
    void viewerCannotCreateDocument() {
        loginPage.openPage()
            .loginAs("viewer", "viewer123")
            .shouldBeOpened()
            .shouldHaveCurrentUserRole("VIEWER")
            .createDocument("Viewer forbidden document")
            .shouldShowDocumentError("Forbidden");
    }

    @Test
    @Tag("smoke")
    void adminCanLoadUsers() {
        loginPage.openPage()
            .loginAs("admin", "admin123")
            .shouldBeOpened()
            .shouldHaveCurrentUserRole("ADMIN")
            .loadAdminUsers()
            .shouldShowAdminUsers();
    }
}

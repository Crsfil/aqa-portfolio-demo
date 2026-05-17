package portfolio.aqa.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class LoginPage {
    @Step("Open login page")
    public LoginPage openPage() {
        open("/");
        executeJavaScript("localStorage.clear()");
        refresh();
        $("[data-testid='login-panel']").shouldBe(visible);
        return this;
    }

    @Step("Login as {username}")
    public DashboardPage loginAs(String username, String password) {
        $("[data-testid='username-input']").setValue(username);
        $("[data-testid='password-input']").setValue(password);
        $("[data-testid='login-button']").click();
        return new DashboardPage();
    }

    @Step("Submit invalid credentials")
    public LoginPage submitInvalidCredentials(String username, String password) {
        $("[data-testid='username-input']").setValue(username);
        $("[data-testid='password-input']").setValue(password);
        $("[data-testid='login-button']").click();
        return this;
    }

    @Step("Check login error")
    public LoginPage shouldShowLoginError(String expectedError) {
        $("[data-testid='login-error']").shouldHave(text(expectedError));
        return this;
    }
}

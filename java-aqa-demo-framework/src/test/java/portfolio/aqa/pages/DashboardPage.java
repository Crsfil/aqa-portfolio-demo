package portfolio.aqa.pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    @Step("Check dashboard is opened")
    public DashboardPage shouldBeOpened() {
        $("[data-testid='dashboard-panel']").shouldBe(visible);
        $("[data-testid='projects-list']").shouldBe(visible);
        return this;
    }

    @Step("Check current user role")
    public DashboardPage shouldHaveCurrentUserRole(String role) {
        $("[data-testid='current-user']").shouldHave(text(role));
        return this;
    }

    @Step("Check projects are visible")
    public DashboardPage shouldHaveProjects() {
        $$("[data-testid='project-card']").shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Create document with title {title}")
    public DashboardPage createDocument(String title) {
        $("[data-testid='document-title-input']").setValue(title);
        $("[data-testid='create-document-button']").click();
        return this;
    }

    @Step("Check document was created")
    public DashboardPage shouldShowDocumentCreatedMessage() {
        $("[data-testid='document-success']").shouldHave(text("Created document"));
        return this;
    }

    @Step("Check document creation error")
    public DashboardPage shouldShowDocumentError(String error) {
        $("[data-testid='document-error']").shouldHave(text(error));
        return this;
    }

    @Step("Load admin users")
    public DashboardPage loadAdminUsers() {
        $("[data-testid='load-users-button']").click();
        return this;
    }

    @Step("Check admin users are visible")
    public DashboardPage shouldShowAdminUsers() {
        $$("[data-testid='user-card']").shouldHave(sizeGreaterThan(0));
        return this;
    }
}

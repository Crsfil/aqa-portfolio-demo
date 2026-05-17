package portfolio.aqa.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import portfolio.aqa.api.AdminClient;
import portfolio.aqa.api.AuthClient;
import portfolio.aqa.tests.BaseApiTest;
import portfolio.aqa.testdata.Users;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class RbacApiTest extends BaseApiTest {
    private final AuthClient authClient = new AuthClient();
    private final AdminClient adminClient = new AdminClient();

    @Test
    @Tag("smoke")
    void viewerCannotReadAdminUsers() {
        String viewerToken = authClient.login(Users.VIEWER).token();

        Response response = adminClient.getUsers(viewerToken);

        assertThat(response.statusCode()).isEqualTo(403);
        assertThat(response.jsonPath().getString("error")).isEqualTo("Forbidden");
    }

    @Test
    @Tag("regression")
    void adminCanReadUsers() {
        String adminToken = authClient.login(Users.ADMIN).token();

        Response response = adminClient.getUsers(adminToken);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getList("users")).isNotEmpty();
    }
}

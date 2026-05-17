package portfolio.aqa.tests.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import portfolio.aqa.api.AuthClient;
import portfolio.aqa.model.LoginRequest;
import portfolio.aqa.model.LoginResponse;
import portfolio.aqa.tests.BaseApiTest;
import portfolio.aqa.testdata.Users;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class AuthApiTest extends BaseApiTest {
    private final AuthClient authClient = new AuthClient();

    @Test
    @Tag("smoke")
    void managerCanLogin() {
        LoginResponse response = authClient.login(Users.MANAGER);

        assertThat(response.token()).isNotBlank();
        assertThat(response.user().role()).isEqualTo("MANAGER");
    }

    @Test
    @Tag("regression")
    void invalidPasswordReturnsUnauthorized() {
        int statusCode = authClient.loginAndReturnStatus(new LoginRequest("manager", "wrong-password"));

        assertThat(statusCode).isEqualTo(401);
    }
}

package portfolio.aqa.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import portfolio.aqa.api.AuthClient;
import portfolio.aqa.api.DocumentClient;
import portfolio.aqa.model.CreateDocumentRequest;
import portfolio.aqa.model.DocumentDto;
import portfolio.aqa.tests.BaseApiTest;
import portfolio.aqa.testdata.Users;

import static org.assertj.core.api.Assertions.assertThat;
import static portfolio.aqa.testdata.DocumentBuilder.document;

@Tag("api")
class DocumentApiTest extends BaseApiTest {
    private final AuthClient authClient = new AuthClient();
    private final DocumentClient documentClient = new DocumentClient();

    @Test
    @Tag("regression")
    void managerCanCreateDocument() {
        String managerToken = authClient.login(Users.MANAGER).token();
        CreateDocumentRequest request = document()
            .forProject(101)
            .withTitle("AQA inspection act")
            .withType("ACT")
            .build();

        DocumentDto document = documentClient.createDocument(managerToken, request);

        assertThat(document.title()).isEqualTo(request.title());
        assertThat(document.status()).isEqualTo("DRAFT");
        assertThat(document.projectId()).isEqualTo(request.projectId());
    }

    @Test
    @Tag("regression")
    void shortDocumentTitleReturnsValidationError() {
        String managerToken = authClient.login(Users.MANAGER).token();
        CreateDocumentRequest request = document()
            .withTitle("x")
            .build();

        Response response = documentClient.tryCreateDocument(managerToken, request);

        assertThat(response.statusCode()).isEqualTo(422);
        assertThat(response.jsonPath().getString("error")).contains("Document title");
    }

    @Test
    @Tag("smoke")
    void viewerCannotCreateDocument() {
        String viewerToken = authClient.login(Users.VIEWER).token();

        Response response = documentClient.tryCreateDocument(viewerToken, document().build());

        assertThat(response.statusCode()).isEqualTo(403);
    }
}

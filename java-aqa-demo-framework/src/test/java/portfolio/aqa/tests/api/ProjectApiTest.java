package portfolio.aqa.tests.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import portfolio.aqa.api.AuthClient;
import portfolio.aqa.api.ProjectClient;
import portfolio.aqa.model.ProjectDto;
import portfolio.aqa.tests.BaseApiTest;
import portfolio.aqa.testdata.Users;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class ProjectApiTest extends BaseApiTest {
    private final AuthClient authClient = new AuthClient();
    private final ProjectClient projectClient = new ProjectClient();

    @Test
    @Tag("smoke")
    void managerCanReadProjects() {
        String managerToken = authClient.login(Users.MANAGER).token();

        List<ProjectDto> projects = projectClient.getProjects(managerToken);

        assertThat(projects)
            .extracting(ProjectDto::name)
            .contains("Residential Complex North", "Warehouse Reconstruction");
    }
}

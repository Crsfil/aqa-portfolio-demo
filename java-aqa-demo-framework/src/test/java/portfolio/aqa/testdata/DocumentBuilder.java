package portfolio.aqa.testdata;

import portfolio.aqa.model.CreateDocumentRequest;

public class DocumentBuilder {
    private long projectId = 101;
    private String title = "Autotest document " + System.currentTimeMillis();
    private String type = "ACT";

    public static DocumentBuilder document() {
        return new DocumentBuilder();
    }

    public DocumentBuilder forProject(long projectId) {
        this.projectId = projectId;
        return this;
    }

    public DocumentBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public DocumentBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public CreateDocumentRequest build() {
        return new CreateDocumentRequest(projectId, title, type);
    }
}

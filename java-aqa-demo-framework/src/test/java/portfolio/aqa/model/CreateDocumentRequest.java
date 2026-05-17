package portfolio.aqa.model;

public record CreateDocumentRequest(
    long projectId,
    String title,
    String type
) {
}

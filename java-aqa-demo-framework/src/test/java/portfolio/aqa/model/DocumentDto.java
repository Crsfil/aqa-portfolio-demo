package portfolio.aqa.model;

public record DocumentDto(
    long id,
    long projectId,
    String title,
    String type,
    String status,
    long ownerId
) {
}

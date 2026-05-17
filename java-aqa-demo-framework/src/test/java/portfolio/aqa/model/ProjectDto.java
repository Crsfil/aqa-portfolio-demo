package portfolio.aqa.model;

public record ProjectDto(
    long id,
    String name,
    long budget,
    String status,
    long ownerId
) {
}

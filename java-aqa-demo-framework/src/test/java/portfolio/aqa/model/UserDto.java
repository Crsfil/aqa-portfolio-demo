package portfolio.aqa.model;

public record UserDto(
    long id,
    String username,
    String name,
    String role
) {
}

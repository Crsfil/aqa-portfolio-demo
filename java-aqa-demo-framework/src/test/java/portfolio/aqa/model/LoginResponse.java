package portfolio.aqa.model;

public record LoginResponse(
    String token,
    UserDto user
) {
}

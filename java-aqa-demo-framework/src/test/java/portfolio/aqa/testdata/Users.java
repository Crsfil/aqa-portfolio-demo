package portfolio.aqa.testdata;

import portfolio.aqa.model.LoginRequest;

public final class Users {
    public static final LoginRequest ADMIN = new LoginRequest("admin", "admin123");
    public static final LoginRequest MANAGER = new LoginRequest("manager", "manager123");
    public static final LoginRequest VIEWER = new LoginRequest("viewer", "viewer123");

    private Users() {
    }
}

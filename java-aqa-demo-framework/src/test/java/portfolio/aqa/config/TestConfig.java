package portfolio.aqa.config;

public final class TestConfig {
    private TestConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("baseUrl", "http://localhost:3000");
    }

    public static String browser() {
        return System.getProperty("browser", "chrome");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }
}

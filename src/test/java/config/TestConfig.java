package config;

public final class TestConfig {

    private TestConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("baseUrl", "https://book-club.qa.guru/");
    }

    public static String baseApiPath() {
        return System.getProperty("baseApiPath", "/api");
    }

    public static String browser() {
        return System.getProperty("browser", "chrome");
    }

    public static String browserSize() {
        return System.getProperty("browserSize", "1920x1080");
    }

    public static String browserVersion() {
        return System.getProperty("browserVersion", "128.0");
    }

    public static String remoteUrl() {
        String remote = System.getProperty("remoteUrl");
        return remote != null ? remote : "";
    }
}
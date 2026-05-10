package tests.UI;

import io.github.bonigarcia.wdm.WebDriverManager;
import api.ApiClient;
import config.TestConfig;
import helpers.Attach;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    protected static final ApiClient api = new ApiClient();

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        String baseUrl = TestConfig.baseUrl();
        Configuration.baseUrl = baseUrl;
        System.setProperty("selenide.baseUrl", baseUrl);
        RestAssured.baseURI = "http://localhost:8000";
        RestAssured.basePath = "/api/v1";
        Configuration.browser = TestConfig.browser();
        Configuration.browserSize = TestConfig.browserSize();
        Configuration.timeout = 10000;
        Configuration.pageLoadStrategy = "eager";

        String remote = TestConfig.remoteUrl();
        if (!remote.isEmpty()) {
            Configuration.remote = remote;
        }
    }

    @BeforeEach
    void addAllureSelenideListener() {
        SelenideLogger.addListener(
                "AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}
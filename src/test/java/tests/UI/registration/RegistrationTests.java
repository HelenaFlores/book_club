package tests.UI.registration;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.TestDataBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.RegistrationPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Регистрация")
public class RegistrationTests extends TestBase {

    String accessToken;
    HomePage homePage = new HomePage();
    RegistrationPage registrationPage = new RegistrationPage();
    private TestDataBuilder testData;
    private AuthComponent auth;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withRegistrationData();

        auth = new AuthComponent(api);
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Открытие страницы регистрации")
    public void OpenRegistrationPageTests() {
        open(baseUrl);

        homePage.openRegistrationPage();

        registrationPage
                .registrationButtonVisible()
                .loginInputVisible()
                .passwordInputVisible();
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void SuccessfulRegistrationTests() {
        open(baseUrl);

        homePage.openRegistrationPage();

        registrationPage
                .setLoginInput(testData.getUsername())
                .setPasswordInput(testData.getPassword())
                .setConfirmPasswordInput(testData.getPassword())
                .clickRegistrationButton();

        homePage.profileButtonVisible()
                .clubsEmptyVisible();

        accessToken = auth.loginAfterUiRegistration(testData.getUsername(), testData.getPassword());
    }
}

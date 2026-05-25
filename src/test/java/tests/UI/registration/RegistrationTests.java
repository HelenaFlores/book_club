package tests.UI.registration;

import api.UsersApiClient;
import components.AuthComponent;
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

    String username;
    String password;
    String accessToken;
    HomePage homePage = new HomePage();
    RegistrationPage registrationPage = new RegistrationPage();
    private AuthComponent auth;

    @BeforeEach
    public void prepareTestData() {

        username = "user_" + System.nanoTime();
        password = "pass_" + System.nanoTime();
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
    public void SuccessfulRegistrationTests() throws InterruptedException {
        open(baseUrl);

        homePage.openRegistrationPage();

        registrationPage
                .setLoginInput(username)
                .setPasswordInput(password)
                .setConfirmPasswordInput(password)
                .clickRegistrationButton();

        homePage.profileButtonVisible()
                .clubsEmptyVisible();

        accessToken = auth.loginAfterUiRegistration(username, password);
    }
}

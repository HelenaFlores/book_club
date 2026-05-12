package tests.UI.registration;

import api.UsersApiClient;
import models.users.login.LoginBodyModel;
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

        HomePage homePage = new HomePage();
        RegistrationPage registrationPage = new RegistrationPage();

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

        HomePage homePage = new HomePage();
        RegistrationPage registrationPage = new RegistrationPage();

        open(baseUrl);

        homePage.openRegistrationPage();

        registrationPage
                .setLoginInput(username)
                .setPasswordInput(password)
                .setConfirmPasswordInput(password)
                .clickRegistrationButton();

        homePage.profileButtonVisible()
                .clubsEmptyVisible();

        LoginBodyModel loginData =
                new LoginBodyModel(username, password);
        accessToken = api.auth.loginAndGetAccessToken(loginData);
    }
}

package tests.UI.login;

import api.UsersApiClient;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.LoginPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Авторизация")
public class LoginTests extends TestBase {

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
    @DisplayName("Открытие страницы авторизации")
    public void OpenLoginPageTests() {
        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();

        open(baseUrl);

        homePage.openLoginPage();

        loginPage
                .loginInputVisible()
                .passwordInputVisible()
                .loginButtonVisible();
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void SuccessfulLoginTests() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();

        open(baseUrl);

        homePage.openLoginPage();

        loginPage
                .setLoginInput(username)
                .setPasswordInput(password)
                .clickLoginButton();

        homePage.profileButtonVisible()
                .clubsEmptyVisible();

        LoginBodyModel loginData =
                new LoginBodyModel(username, password);
        accessToken = api.auth.loginAndGetAccessToken(loginData);
    }
}

package tests.UI.login;

import api.UsersApiClient;
import helpers.TestDataBuilder;
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

    String accessToken;
    HomePage homePage = new HomePage();
    LoginPage loginPage = new LoginPage();
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser();
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
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        open(baseUrl);

        homePage.openLoginPage();

        loginPage
                .setLoginInput(testData.getUsername())
                .setPasswordInput(testData.getPassword())
                .clickLoginButton();

        homePage.profileButtonVisible()
                .clubsEmptyVisible();

        LoginBodyModel loginData =
                new LoginBodyModel(testData.getUsername(), testData.getPassword());
        accessToken = api.auth.loginAndGetAccessToken(loginData);
    }
}

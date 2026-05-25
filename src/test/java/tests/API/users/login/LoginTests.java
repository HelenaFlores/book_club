package tests.API.users.login;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.users.login.*;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@DisplayName("[API] Авторизация")
public class LoginTests extends TestBase {

    String accessToken;
    boolean userCreated;
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser();
    }

    @AfterEach
    public void after() {
        if (!userCreated) {
            return;
        }

        try {
            if (accessToken == null) {
                LoginBodyModel loginData = new LoginBodyModel(testData.getUsername(), testData.getPassword());
                accessToken = api.auth.loginAndGetAccessToken(loginData);
            }
            if (accessToken != null) {
                UsersApiClient.deleteUser(accessToken);
            }
        } catch (Exception e) {
            System.err.println("Failed to cleanup test user: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void successfulLoginTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);
        userCreated = true;

        LoginBodyModel loginData = new LoginBodyModel(testData.getUsername(), testData.getPassword());

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        accessToken = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(accessToken).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(accessToken).isNotEqualTo(actualRefresh);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void wrongCredentialsLoginTest() {
        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);
        userCreated = true;

        LoginBodyModel loginData = new LoginBodyModel(testData.getUsername(), testData.getPassword() + ADDITIONAL_SYMBOLS);
        WrongCredentialsLoginResponseModel loginResponse =
                api.auth.loginWrongCredentials(loginData);

        String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    public void wrongLoginNullUsernameTest() {
        LoginBodyModel loginData =
                new LoginBodyModel(LOGIN_WRONG_PASSWORD_OR_USERNAME_NULL, testData.getPassword());

        WrongLoginNullUsernameResponseModel loginResponse =
                api.auth.wrongLoginNullUsernameResponse(loginData);

        String expectedDetailError = LOGIN_WRONG_PASSWORD_OR_USERNAME_NULL_ERROR;
        String actualDetailError = loginResponse.username().get(0);
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    @DisplayName("Авторизация с null паролем")
    public void wrongPasswordNullTest() {
        LoginBodyModel loginData =
                new LoginBodyModel(testData.getUsername(), LOGIN_WRONG_PASSWORD_OR_USERNAME_NULL);

        WrongLoginNullPasswordResponseModel loginResponse =
                api.auth.wrongLoginNullPasswordResponse(loginData);

        String expectedDetailError = LOGIN_WRONG_PASSWORD_OR_USERNAME_NULL_ERROR;
        String actualDetailError = loginResponse.password().get(0);
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }
}

package tests.API.users.registration;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.users.login.LoginBodyModel;
import models.users.registration.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@DisplayName("[API] Регистрация")
public class RegistrationTests extends TestBase {

    boolean userCreated;
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withRegistrationData();
    }

    @AfterEach
    public void after() {
        if (!userCreated) {
            return;
        }

        try {
            LoginBodyModel loginData = new LoginBodyModel(testData.getRegistrationData().username(), testData.getRegistrationData().password());
            String accessToken = api.auth.loginAndGetAccessToken(loginData);
            if (accessToken != null) {
                UsersApiClient.deleteUser(accessToken);
            }
        } catch (Exception e) {
            System.err.println("Failed to cleanup test user: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = testData.getRegistrationData();

        SuccessfulRegistrationResponseModel registrationResponse =
                api.users.register(registrationData);
        userCreated = true;

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(testData.getUsername());
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = testData.getRegistrationData();

        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                api.users.register(registrationData);
        userCreated = true;

        assertThat(firstRegistrationResponse.username()).isEqualTo(testData.getRegistrationData().username());

        ExistingUserResponseModel secondRegistrationResponse =
                api.users.registerExistingUser(registrationData);

        String expectedError = REGISTRATION_EXISTING_USER_ERROR;
        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("Регистрация без поля пароля")
    public void wrongRegistrationWithoutPasswordTest() {
        RegistrationBodyWithoutPasswordModel registrationData = new RegistrationBodyWithoutPasswordModel(testData.getUsername());

        WrongRegistrationWithoutPasswordResponseModel registrationResponse =
                api.users.registerWithoutPassword(registrationData);

        String expectedError = REGISTRATION_WRONG_WITHOUT_PASSWORD_OR_LOGIN;
        String actualError = registrationResponse.password().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("Регистрация без поля логина")
    public void wrongRegistrationWithoutLoginTest() {
        RegistrationBodyWithoutLoginModel registrationData = new RegistrationBodyWithoutLoginModel(testData.getPassword());

        WrongRegistrationWithoutLoginResponseModel registrationResponse =
                api.users.registerWithoutLogin(registrationData);

        String expectedError = REGISTRATION_WRONG_WITHOUT_PASSWORD_OR_LOGIN;
        String actualError = registrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }


}
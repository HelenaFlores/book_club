package tests.API.users.registration;

import api.UsersApiClient;
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

    String username;
    String password;
    RegistrationBodyModel registrationData;
    boolean userCreated;

    @BeforeEach
    public void prepareTestData() {

        username = "user_" + System.nanoTime();
        password = "pass_" + System.nanoTime();
        registrationData = new RegistrationBodyModel(username, password);
        userCreated = false;
    }

    @AfterEach
    public void after() {
        if (!userCreated) {  // ← пользователь не создался — cleanup не нужен
            return;
        }

        try {
            LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
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
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse =
                api.users.register(registrationData);
        userCreated = true;

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                api.users.register(registrationData);
        userCreated = true;

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse =
                api.users.registerExistingUser(registrationData);

        String expectedError = REGISTRATION_EXISTING_USER_ERROR;
        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("Регистрация без поля пароля")
    public void wrongRegistrationWithoutPasswordTest() {
        RegistrationBodyWithoutPasswordModel registrationData = new RegistrationBodyWithoutPasswordModel(username);

        WrongRegistrationWithoutPasswordResponseModel registrationResponse =
                api.users.registerWithoutPassword(registrationData);

        String expectedError = REGISTRATION_WRONG_WITHOUT_PASSWORD_OR_LOGIN;
        String actualError = registrationResponse.password().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    @DisplayName("Регистрация без поля логина")
    public void wrongRegistrationWithoutLoginTest() {
        RegistrationBodyWithoutLoginModel registrationData = new RegistrationBodyWithoutLoginModel(password);

        WrongRegistrationWithoutLoginResponseModel registrationResponse =
                api.users.registerWithoutLogin(registrationData);

        String expectedError = REGISTRATION_WRONG_WITHOUT_PASSWORD_OR_LOGIN;
        String actualError = registrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }


}
package tests.users.logout;

import api.UsersApiClient;
import models.users.login.LoginBodyModel;
import models.users.logout.LogoutBodyModel;
import models.users.logout.LogoutEmptyBodyModel;
import models.users.logout.WrongLogoutNoValidTokenResponseModel;
import models.users.logout.WrongLogoutWithoutTokenResponseModel;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    String username;
    String password;
    String accessToken;
    boolean userCreated;

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
        userCreated = false;
        accessToken = null;
    }

    @AfterEach
    public void after() {
        if (!userCreated) {
            return;
        }

        try {
            if (accessToken == null) {
                LoginBodyModel loginData = new LoginBodyModel(username, password);
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
    public void successfulLogoutTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);
        userCreated = true;

        LoginBodyModel loginData = new LoginBodyModel(username, password);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
                api.auth.logout(logoutData);
    }


    @Test
    public void noValidTokenLogoutTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);
        userCreated = true;

        LoginBodyModel loginData = new LoginBodyModel(username, password);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData =
                new LogoutBodyModel(refreshToken + ADDITIONAL_SYMBOLS);
        WrongLogoutNoValidTokenResponseModel logoutResponse =
                api.auth.logoutNoValidToken(logoutData);

        String expectedDetailError = LOGOUT_WRONG_DETAIL_ERROR;
        String expectedCodeError = LOGOUT_WRONG_CODE_ERROR;
        String actualDetailError = logoutResponse.detail();
        String actualCodeError = logoutResponse.code();
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
        assertThat(actualCodeError).isEqualTo(expectedCodeError);
    }

    @Test
    public void wrongWithoutRefreshTokenLogoutTest() {
        LogoutEmptyBodyModel logoutData = new LogoutEmptyBodyModel();
        WrongLogoutWithoutTokenResponseModel logoutResponse =
                api.auth.logoutWithoutRefreshToken(logoutData);

        String expectedDetailError = LOGOUT_WRONG_REFRESH_ERROR;
        String actualRefreshError = logoutResponse.refresh().get(0);
        assertThat(actualRefreshError).isEqualTo(expectedDetailError);
    }
}

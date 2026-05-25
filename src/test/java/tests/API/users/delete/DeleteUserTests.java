package tests.API.users.delete;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

@DisplayName("[API] Удаление пользователя")
public class DeleteUserTests extends TestBase {

    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withBook();
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    public void successfulDeleteUserTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        UsersApiClient.deleteUser(accessToken);
    }
}

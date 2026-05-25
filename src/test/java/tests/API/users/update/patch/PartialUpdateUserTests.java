package tests.API.users.update.patch;

import api.AuthApiClient;
import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import models.users.update.PartialUpdateBodyModel;
import models.users.update.SuccessfulUpdateResponseModel;
import models.users.update.WrongUpdateMethodAllowedResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.UPDATE_WRONG_DETAIL_ERROR;

@DisplayName("[API] Редактирование профиля PATCH запросом")
public class PartialUpdateUserTests extends TestBase {

    String accessToken;
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
    @DisplayName("Успешное редактирование пользователя")
    public void successfulPartialUpdateTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        PartialUpdateBodyModel updateData = new PartialUpdateBodyModel(testData.getUsername(), testData.getPassword());
        SuccessfulUpdateResponseModel updateResponse = AuthApiClient.putPartialUpdate(accessToken, updateData);

        String userNameData = updateData.username();
        String userNameResponse = updateResponse.username();
        assertThat(userNameData).isEqualTo(userNameResponse);
    }

    @Test
    @DisplayName("Редактирование пользователя с неверным методом")
    public void wrongMethodAllowedPartialUpdateTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        PartialUpdateBodyModel updateData = new PartialUpdateBodyModel(testData.getUsername(), testData.getPassword());
        WrongUpdateMethodAllowedResponseModel updateResponse = AuthApiClient.errorMethodAllowedPatchUpdate(accessToken, updateData);

        String expectedDetailError = UPDATE_WRONG_DETAIL_ERROR;
        String actualDetailError = String.valueOf(updateResponse.detail());
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }
}


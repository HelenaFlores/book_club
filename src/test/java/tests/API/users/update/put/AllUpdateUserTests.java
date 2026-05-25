package tests.API.users.update.put;

import api.AuthApiClient;
import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import models.users.update.AllUpdateBodyModel;
import models.users.update.SuccessfulUpdateResponseModel;
import models.users.update.WrongUpdateMethodAllowedResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.UPDATE_WRONG_DETAIL_ERROR;

@DisplayName("[API] Редактирование профиля PUT запросом")
public class AllUpdateUserTests extends TestBase {

    String accessToken;
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withPersonalInfo();
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное редактирование пользователя")
    public void successfulAllUpdateTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        AllUpdateBodyModel updateData = new AllUpdateBodyModel(testData.getUsername(), testData.getFirstname(), testData.getLastName(), testData.getEmail());
        SuccessfulUpdateResponseModel updateResponse = AuthApiClient.putUpdate(accessToken, updateData);

        String userNameData = updateData.username();
        String userNameResponse = updateResponse.username();
        assertThat(userNameData).isEqualTo(userNameResponse);
    }

    @Test
    @DisplayName("Редактирование пользователя с неверным методом")
    public void wrongMethodAllowedAllUpdateTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        AllUpdateBodyModel updateData = new AllUpdateBodyModel(testData.getUsername(), testData.getFirstname(), testData.getLastName(), testData.getEmail());
        WrongUpdateMethodAllowedResponseModel updateResponse = AuthApiClient.errorMethodAllowedPutUpdate(accessToken, updateData);

        String expectedDetailError = UPDATE_WRONG_DETAIL_ERROR;
        String actualDetailError = String.valueOf(updateResponse.detail());
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }
}

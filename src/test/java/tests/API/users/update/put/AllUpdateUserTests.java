package tests.API.users.update.put;

import api.UsersApiClient;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import models.users.update.AllUpdateBodyModel;
import models.users.update.SuccessfulUpdateResponseModel;
import models.users.update.WrongUpdateMethodAllowedResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

@DisplayName("[API] Редактирование профиля PUT запросом")
public class AllUpdateUserTests extends TestBase {

    Faker faker = new Faker();
    String username;
    String password;
    String firstname;
    String lastName;
    String email;
    String accessToken;

    @BeforeEach
    public void prepareTestData() {

        firstname = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
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
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
                        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        AllUpdateBodyModel updateData = new AllUpdateBodyModel(username, firstname, lastName, email);
        SuccessfulUpdateResponseModel updateResponse = api.auth.putUpdate(accessToken, updateData);

        String userNameData = updateData.username();
        String userNameResponse = updateResponse.username();
        assertThat(userNameData).isEqualTo(userNameResponse);
    }

    @Test
    @DisplayName("Редактирование пользователя с неверным методом")
    public void wrongMethodAllowedAllUpdateTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
                        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        AllUpdateBodyModel updateData = new AllUpdateBodyModel(username, firstname, lastName, email);
        WrongUpdateMethodAllowedResponseModel updateResponse = api.auth.errorMethodAllowedPutUpdate(accessToken, updateData);

        String expectedDetailError = UPDATE_WRONG_DETAIL_ERROR;
        String actualDetailError = String.valueOf(updateResponse.detail());
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }
}

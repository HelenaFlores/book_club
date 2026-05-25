package tests.API.clubs.create;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[API] Создание клуба")
public class CreateClubTests extends TestBase {

    String accessToken;
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withBook();
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное создание клуба")
    public void successfulCreateClubTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        CreateClubBodyModel createClubBody = new CreateClubBodyModel(
                testData.getBookTitle(),
                testData.getBookAuthors(),
                testData.getPublicationYear(),
                testData.getDescription(),
                testData.getTelegramChatLink()
        );
        SuccessfulCreateClubResponseModel createClubResponse =
                api.clubs.createClub(accessToken, createClubBody);

        assertThat(createClubResponse.id()).isGreaterThan(0);
        assertThat(createClubResponse.bookTitle()).isEqualTo(createClubBody.bookTitle());
        assertThat(createClubResponse.bookAuthors()).isEqualTo(createClubBody.bookAuthors());
        assertThat(createClubResponse.publicationYear()).isEqualTo(createClubBody.publicationYear());
        assertThat(createClubResponse.description()).isEqualTo(createClubBody.description());
        assertThat(createClubResponse.telegramChatLink()).isEqualTo(createClubBody.telegramChatLink());
        assertThat(createClubResponse.owner()).isGreaterThan(0);
        assertThat(createClubResponse.members()).isNotNull().isNotEmpty();
        assertThat(createClubResponse.created()).isNotBlank();
    }
}

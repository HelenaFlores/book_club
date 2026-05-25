package tests.API.clubs.getbyid;

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

@DisplayName("[API] Просмотр клуба по id")
public class GetClubByIdTests extends TestBase {

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
    @DisplayName("Успешный просмотр клуба по id")
    public void successfulGetClubByIdTest() {
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

        SuccessfulCreateClubResponseModel getClubResponse =
                api.clubs.getClubById(accessToken, createClubResponse.id());

        assertThat(getClubResponse.id()).isEqualTo(createClubResponse.id());
        assertThat(getClubResponse.bookTitle()).isEqualTo(createClubBody.bookTitle());
        assertThat(getClubResponse.bookAuthors()).isEqualTo(createClubBody.bookAuthors());
        assertThat(getClubResponse.publicationYear()).isEqualTo(createClubBody.publicationYear());
        assertThat(getClubResponse.description()).isEqualTo(createClubBody.description());
        assertThat(getClubResponse.telegramChatLink()).isEqualTo(createClubBody.telegramChatLink());
        assertThat(getClubResponse.owner()).isGreaterThan(0);
        assertThat(getClubResponse.members()).isNotNull().isNotEmpty();
        assertThat(getClubResponse.reviews()).isNotNull();
        assertThat(getClubResponse.created()).isNotBlank();
    }
}

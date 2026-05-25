package tests.API.clubs.update;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.clubs.update.SuccessfulUpdateClubResponseModel;
import models.clubs.update.UpdateClubBodyModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[API] Редактирование клуба")
public class UpdateClubTests extends TestBase {

    private final Faker faker = new Faker();
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
    @DisplayName("Успешное редактирование клуба PATCH запросом")
    public void successfulPatchUpdateClubTest() {
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

        String updatedBookTitle = faker.book().title() + "_updated";
        String updatedDescription = faker.lorem().sentence(15);

        UpdateClubBodyModel updateClubBody = new UpdateClubBodyModel(
                updatedBookTitle,
                testData.getBookAuthors(),
                testData.getPublicationYear(),
                updatedDescription,
                testData.getTelegramChatLink()
        );
        SuccessfulUpdateClubResponseModel updateClubResponse =
                api.clubs.updateClub(accessToken, createClubResponse.id(), updateClubBody);

        assertThat(updateClubResponse.id()).isEqualTo(createClubResponse.id());
        assertThat(updateClubResponse.bookTitle()).isEqualTo(updatedBookTitle);
        assertThat(updateClubResponse.description()).isEqualTo(updatedDescription);
        assertThat(updateClubResponse.owner()).isEqualTo(createClubResponse.owner());
        assertThat(updateClubResponse.modified()).isNotBlank();
    }

    @Test
    @DisplayName("Успешное редактирование клуба PUT запросом")
    public void successfulPutUpdateClubTest() {
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

        String updatedBookTitle = faker.book().title() + "_updated";
        String updatedDescription = faker.lorem().sentence(15);

        UpdateClubBodyModel updateClubBody = new UpdateClubBodyModel(
                updatedBookTitle,
                testData.getBookAuthors(),
                testData.getPublicationYear(),
                updatedDescription,
                testData.getTelegramChatLink()
        );
        SuccessfulUpdateClubResponseModel updateClubResponse =
                api.clubs.updatePutClub(accessToken, createClubResponse.id(), updateClubBody);

        assertThat(updateClubResponse.id()).isEqualTo(createClubResponse.id());
        assertThat(updateClubResponse.bookTitle()).isEqualTo(updatedBookTitle);
        assertThat(updateClubResponse.description()).isEqualTo(updatedDescription);
        assertThat(updateClubResponse.owner()).isEqualTo(createClubResponse.owner());
        assertThat(updateClubResponse.modified()).isNotBlank();
    }
}

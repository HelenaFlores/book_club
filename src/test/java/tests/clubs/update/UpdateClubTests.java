package tests.clubs.update;

import api.UsersApiClient;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.clubs.update.SuccessfulUpdateClubResponseModel;
import models.clubs.update.UpdateClubBodyModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateClubTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;
    String accessToken;

    @BeforeEach
    public void prepareTestData() {
        long uniqueSuffix = System.currentTimeMillis();
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();

        bookTitle = faker.book().title() + "_" + uniqueSuffix;
        bookAuthors = faker.book().author();
        publicationYear = faker.number().numberBetween(1900, 2026);
        description = faker.lorem().sentence(10);
        telegramChatLink = "https://t.me/club_" + uniqueSuffix;
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    public void successfulUpdateClubTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        CreateClubBodyModel createClubBody = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );
        SuccessfulCreateClubResponseModel createClubResponse =
                api.clubs.createClub(accessToken, createClubBody);

        String updatedBookTitle = faker.book().title() + "_updated";
        String updatedDescription = faker.lorem().sentence(15);

        UpdateClubBodyModel updateClubBody = new UpdateClubBodyModel(
                updatedBookTitle,
                bookAuthors,
                publicationYear,
                updatedDescription,
                telegramChatLink
        );
        SuccessfulUpdateClubResponseModel updateClubResponse =
                api.clubs.updateClub(accessToken, createClubResponse.id(), updateClubBody);

        assertThat(updateClubResponse.id()).isEqualTo(createClubResponse.id());
        assertThat(updateClubResponse.bookTitle()).isEqualTo(updatedBookTitle);
        assertThat(updateClubResponse.description()).isEqualTo(updatedDescription);
        assertThat(updateClubResponse.owner()).isEqualTo(createClubResponse.owner());
        assertThat(updateClubResponse.modified()).isNotBlank();
    }
}

package tests.clubs.create;

import api.UsersApiClient;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.clubs.delete.DeleteClubBodyModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateClubTests extends TestBase {

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
    public void successfulCreateClubTest() {
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

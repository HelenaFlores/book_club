package tests.users.delete;

import api.ClubsApiClient;
import api.UsersApiClient;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TestBase;

public class DeleteUserTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;

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

    @Test
    public void successfulDeleteUserTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        UsersApiClient.deleteUser(accessToken);
    }
}

package tests.reviews.create;

import api.UsersApiClient;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class CreateReviewsTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    int club;
    String review;
    int assessment;
    int readPages;
    String accessToken;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();

        long uniqueSuffix = System.currentTimeMillis();
        club = faker.number().positive();
        assessment = faker.number().numberBetween(1, 4);
        review = faker.book().title() + "_" + uniqueSuffix;
        readPages = faker.number().positive();

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
    public void successfulCreateReviewTest() {
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

        CreateReviewsBodyModel createReviewsBody = new CreateReviewsBodyModel(
                createClubResponse.id(),
                review,
                assessment,
                readPages
        );
        SuccessfulCreateReviewsResponseModel createReviewsResponse =
                api.reviews.createReviews(accessToken, createReviewsBody);

        Instant responseInstant = Instant.parse(createReviewsResponse.created());
        Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

        assertThat(createReviewsResponse.id()).isGreaterThan(0);
        assertThat(createReviewsResponse.club()).isEqualTo(createReviewsBody.club());
        assertThat(createReviewsResponse.review()).isEqualTo(createReviewsBody.review());
        assertThat(createReviewsResponse.assessment()).isEqualTo(createReviewsBody.assessment());
        assertThat(createReviewsResponse.readPages()).isEqualTo(createReviewsBody.readPages());
        assertThat(responseInstant).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
        assertThat(createReviewsResponse.modified()).isNull();
    }
}

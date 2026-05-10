package tests.API.reviews.get;

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
import tests.API.TestBase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class GetReviewsByIdTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;
    String accessToken;
    String review;
    int assessment;
    int readPages;

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

        assessment = faker.number().numberBetween(1, 4);
        review = faker.book().title() + "_" + uniqueSuffix;
        readPages = faker.number().positive();
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    public void successfulGetReviewsByIdTest() {
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

        SuccessfulCreateReviewsResponseModel getReviewsResponse =
                api.reviews.getReviewsById(accessToken, createReviewsResponse.id());

        assertThat(getReviewsResponse.id()).isEqualTo(createReviewsResponse.id());
        assertThat(getReviewsResponse.club()).isEqualTo(createClubResponse.id());
        assertThat(getReviewsResponse.review()).isEqualTo(createReviewsBody.review());
        assertThat(getReviewsResponse.assessment()).isEqualTo(createReviewsBody.assessment());
        assertThat(getReviewsResponse.readPages()).isEqualTo(createReviewsBody.readPages());
        assertThat(responseInstant).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
        assertThat(getReviewsResponse.modified()).isNull();

    }
}

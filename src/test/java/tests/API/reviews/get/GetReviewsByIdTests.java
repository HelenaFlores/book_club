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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("[API] Просмотр отзыва")
public class GetReviewsByIdTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String usernameSecond;
    String passwordSecond;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;
    String accessToken;
    String accessTokenSecond;
    String review;
    int assessment;
    int readPages;

    @BeforeEach
    public void prepareTestData() {
        long uniqueSuffix = System.nanoTime();
        username = "user_" + + System.nanoTime();
        password = "pass_" + + System.nanoTime();

        usernameSecond = "user_" + + System.nanoTime();
        passwordSecond = "pass_" + + System.nanoTime();

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
        if (accessTokenSecond != null) {
            UsersApiClient.deleteUser(accessTokenSecond);
        }
    }

    @Test
    @DisplayName("Успешный просмотр своего отзыва")
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

    @Test
    @DisplayName("Успешный просмотр чужого отзыва")
    public void successfulGetReviewsInStrangerByIdTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        RegistrationBodyModel registrationDataSecond = new RegistrationBodyModel(usernameSecond, passwordSecond);
        api.users.register(registrationDataSecond);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        LoginBodyModel loginDataSecond =
                new LoginBodyModel(usernameSecond, passwordSecond);
        accessTokenSecond = api.auth.loginAndGetAccessToken(loginDataSecond);

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
                api.reviews.getReviewsById(accessTokenSecond, createReviewsResponse.id());

        assertThat(getReviewsResponse.id()).isEqualTo(createReviewsResponse.id());
        assertThat(getReviewsResponse.club()).isEqualTo(createClubResponse.id());
        assertThat(getReviewsResponse.review()).isEqualTo(createReviewsBody.review());
        assertThat(getReviewsResponse.assessment()).isEqualTo(createReviewsBody.assessment());
        assertThat(getReviewsResponse.readPages()).isEqualTo(createReviewsBody.readPages());
        assertThat(responseInstant).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
        assertThat(getReviewsResponse.modified()).isNull();

    }
}

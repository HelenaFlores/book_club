package tests.API.reviews.update;

import api.UsersApiClient;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.reviews.update.InvalidClubUpdateReviewsResponseModel;
import models.reviews.update.SuccessfulUpdateReviewsResponseModel;
import models.reviews.update.UpdateReviewsBodyModel;
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
import static tests.TestData.WITHOUT_AUTH_DETAIL_ERROR;
import static tests.TestData.getInvalidClubDetailError;

public class UpdateReviewsTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String usernameSecond;
    String passwordSecond;
    String review;
    int assessment;
    int readPages;
    String accessToken;
    String accessTokenSecond;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;
    String updatedReview;
    int updatedAssessment;
    int updateReadPages;

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + + System.nanoTime();
        password = "pass_" + + System.nanoTime();

        usernameSecond = "user_" + + System.nanoTime();
        passwordSecond = "pass_" + + System.nanoTime();


        long uniqueSuffix = System.currentTimeMillis();
        assessment = faker.number().numberBetween(1, 4);
        review = faker.book().title() + "_" + uniqueSuffix;
        readPages = faker.number().positive();

        bookTitle = faker.book().title() + "_" + uniqueSuffix;
        bookAuthors = faker.book().author();
        publicationYear = faker.number().numberBetween(1900, 2026);
        description = faker.lorem().sentence(10);
        telegramChatLink = "https://t.me/club_" + uniqueSuffix;

        updatedReview = faker.book().title() + "_updated";
        updatedAssessment = faker.number().numberBetween(1, 4);
        updateReadPages = faker.number().positive();
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    public void successfulUpdateReviewsTest() {
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

        UpdateReviewsBodyModel updateReviewsBody = new UpdateReviewsBodyModel(
                createReviewsResponse.id(),
                updatedReview,
                updatedAssessment,
                updateReadPages
        );

        SuccessfulUpdateReviewsResponseModel updateReviewsResponse =
                api.reviews.updateReviews(accessToken, createReviewsResponse.id(), updateReviewsBody);

        Instant responseInstantCreated = Instant.parse(updateReviewsResponse.created());
        Instant responseInstantModified = Instant.parse(updateReviewsResponse.modified());
        Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

        assertThat(updateReviewsResponse.id()).isGreaterThan(0);
        assertThat(updateReviewsResponse.club()).isEqualTo(updateReviewsBody.club());
        assertThat(updateReviewsResponse.review()).isEqualTo(updatedReview);
        assertThat(updateReviewsResponse.assessment()).isEqualTo(updatedAssessment);
        assertThat(updateReviewsResponse.readPages()).isEqualTo(updateReadPages);
        assertThat(responseInstantCreated).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
        assertThat(responseInstantModified).isCloseTo(currentInstant, within(2, ChronoUnit.SECONDS));
    }

    @Test
    public void strangerClubUpdateReviewsTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        RegistrationBodyModel registrationDataSecond = new RegistrationBodyModel(usernameSecond, passwordSecond);
        api.users.register(registrationDataSecond);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        LoginBodyModel loginDataSecond =new LoginBodyModel(registrationDataSecond.username(), registrationData.password());
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

        UpdateReviewsBodyModel updateReviewsBody = new UpdateReviewsBodyModel(
                createReviewsResponse.id(),
                updatedReview,
                updatedAssessment,
                updateReadPages
        );

        InvalidClubUpdateReviewsResponseModel updateReviewsResponse =
                api.reviews.invalidClubUpdateReviews(accessTokenSecond, createReviewsResponse.id(), updateReviewsBody);

        String expectedError = getInvalidClubDetailError(createReviewsResponse.id());
        assertThat(updateReviewsResponse.club().get(0)).isEqualTo(expectedError);
    }
}

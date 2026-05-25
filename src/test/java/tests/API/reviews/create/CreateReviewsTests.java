package tests.API.reviews.create;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.CreateReviewsWithoutAuthResponseModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static tests.TestData.WITHOUT_AUTH_DETAIL_ERROR;

@DisplayName("[API] Создание отзыва")
public class CreateReviewsTests extends TestBase {

    String accessToken;
    String accessTokenSecond;
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withSecondUser()
                .withBook()
                .withReview();
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
    @DisplayName("Успешное создание отзыва в свой клуб")
    public void successfulCreateReviewsTest() {
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

        CreateReviewsBodyModel createReviewsBody = new CreateReviewsBodyModel(
                createClubResponse.id(),
                testData.getReview(),
                testData.getAssessment(),
                testData.getReadPages()
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

    @Test
    @DisplayName("Успешное создание отзыва в чужой клуб")
    public void createReviewsInStrangerClubTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        RegistrationBodyModel registrationDataSecond = new RegistrationBodyModel(testData.getUsernameSecond(), testData.getPasswordSecond());
        api.users.register(registrationDataSecond);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        accessToken = api.auth.loginAndGetAccessToken(loginData);

        LoginBodyModel loginDataSecond = new LoginBodyModel(testData.getUsernameSecond(), testData.getPasswordSecond());
        accessTokenSecond = api.auth.loginAndGetAccessToken(loginDataSecond);

        CreateClubBodyModel createClubBody = new CreateClubBodyModel(
                testData.getBookTitle(),
                testData.getBookAuthors(),
                testData.getPublicationYear(),
                testData.getDescription(),
                testData.getTelegramChatLink()
        );
        SuccessfulCreateClubResponseModel createClubResponse =
                api.clubs.createClub(accessToken, createClubBody);

        CreateReviewsBodyModel createReviewsBody = new CreateReviewsBodyModel(
                createClubResponse.id(),
                testData.getReview(),
                testData.getAssessment(),
                testData.getReadPages()
        );

        SuccessfulCreateReviewsResponseModel createReviewsResponse =
                api.reviews.createReviews(accessTokenSecond, createReviewsBody);

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

    @Test
    @DisplayName("Ошибка создания отзыва без авторизации")
    public void withoutAuthCreateReviewsTest() {
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

        CreateReviewsBodyModel createReviewsBody = new CreateReviewsBodyModel(
                createClubResponse.id(),
                testData.getReview(),
                testData.getAssessment(),
                testData.getReadPages()
        );
        CreateReviewsWithoutAuthResponseModel createReviewsResponse =
                api.reviews.createReviewsWithoutAuth(createReviewsBody);

        assertThat(createReviewsResponse.detail()).isEqualTo(WITHOUT_AUTH_DETAIL_ERROR);
    }
}

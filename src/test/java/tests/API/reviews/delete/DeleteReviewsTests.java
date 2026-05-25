package tests.API.reviews.delete;

import api.ReviewsApiClient;
import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.reviews.delete.ForbiddenDeleteReviewsResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.FORBIDDEN_ERROR;

@DisplayName("[API] Удаление отзыва")
public class DeleteReviewsTests extends TestBase {

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
    @DisplayName("Успешное удаление своего отзыва")
    public void successfulDeleteReviewsTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

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

        ReviewsApiClient.deleteReviews(accessToken, createReviewsResponse.id());
    }

    @Test
    @DisplayName("Ошибка удаления чужого отзыва")
    public void forbiddenDeleteReviewsTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.getUsername(), testData.getPassword());
        api.users.register(registrationData);

        RegistrationBodyModel registrationDataSecond = new RegistrationBodyModel(testData.getUsernameSecond(), testData.getPasswordSecond());
        api.users.register(registrationDataSecond);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

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
                api.reviews.createReviews(accessToken, createReviewsBody);

        ForbiddenDeleteReviewsResponseModel deleteReviewsResponse =
                ReviewsApiClient.forbiddenDeleteReviews(
                        accessTokenSecond,
                        createReviewsResponse.id());

        assertThat(deleteReviewsResponse.detail()).isEqualTo(FORBIDDEN_ERROR);
    }
}

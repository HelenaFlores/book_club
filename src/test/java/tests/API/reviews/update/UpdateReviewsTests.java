package tests.API.reviews.update;

import api.UsersApiClient;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.reviews.update.ForbiddenUpdateReviewsResponseModel;
import models.reviews.update.SuccessfulUpdateReviewsResponseModel;
import models.reviews.update.UpdateReviewsBodyModel;
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
import static tests.TestData.FORBIDDEN_ERROR;

@DisplayName("[API] Редактирование отзыва")
public class UpdateReviewsTests extends TestBase {

    String accessToken;
    String accessTokenSecond;
    private TestDataBuilder testData;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withSecondUser()
                .withBook()
                .withReview()
                .withUpdatedReview();
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

    @DisplayName("Успешное редактирование отзыва своего клуба")
    @Test
    public void successfulUpdateReviewsTest() {
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

        UpdateReviewsBodyModel updateReviewsBody = new UpdateReviewsBodyModel(
                createClubResponse.id(),
                testData.getUpdatedReview(),
                testData.getUpdatedAssessment(),
                testData.getUpdateReadPages()
        );

        SuccessfulUpdateReviewsResponseModel updateReviewsResponse =
                api.reviews.updateReviews(accessToken, createReviewsResponse.id(), updateReviewsBody);

        Instant responseInstantCreated = Instant.parse(updateReviewsResponse.created());
        Instant responseInstantModified = Instant.parse(updateReviewsResponse.modified());
        Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

        assertThat(updateReviewsResponse.id()).isGreaterThan(0);
        assertThat(updateReviewsResponse.club()).isEqualTo(updateReviewsBody.club());
        assertThat(updateReviewsResponse.review()).isEqualTo(testData.getUpdatedReview());
        assertThat(updateReviewsResponse.assessment()).isEqualTo(testData.getUpdatedAssessment());
        assertThat(updateReviewsResponse.readPages()).isEqualTo(testData.getUpdateReadPages());
        assertThat(responseInstantCreated).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
        assertThat(responseInstantModified).isCloseTo(currentInstant, within(2, ChronoUnit.SECONDS));
    }

    @DisplayName("Ошибка редактирования чужого отзыва")
    @Test
    public void forbiddenUpdateReviewsTest() {
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
                api.reviews.createReviews(accessToken, createReviewsBody);

        UpdateReviewsBodyModel updateReviewsBody = new UpdateReviewsBodyModel(
                createReviewsResponse.id(),
                testData.getUpdatedReview(),
                testData.getUpdatedAssessment(),
                testData.getUpdateReadPages()
        );

        ForbiddenUpdateReviewsResponseModel updateReviewsResponse =
                api.reviews.forbiddenUpdateReviews(
                        accessTokenSecond,
                        createReviewsResponse.id(),
                        updateReviewsBody);

        assertThat(updateReviewsResponse.detail()).isEqualTo(FORBIDDEN_ERROR);
    }
}

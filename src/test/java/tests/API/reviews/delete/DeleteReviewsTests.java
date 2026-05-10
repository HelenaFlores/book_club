package tests.API.reviews.delete;

import api.ReviewsApiClient;
import api.UsersApiClient;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.reviews.delete.ForbiddenDeleteReviewsResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import tests.API.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.FORBIDDEN_ERROR;

@DisplayName("[API] Удаление отзыва")
public class DeleteReviewsTests extends TestBase {

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
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

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

        ReviewsApiClient.deleteReviews(accessToken, createReviewsResponse.id());
    }

    @Test
    @DisplayName("Ошибка удаления чужого отзыва")
    public void forbiddenDeleteReviewsTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        RegistrationBodyModel registrationDataSecond = new RegistrationBodyModel(usernameSecond, passwordSecond);
        api.users.register(registrationDataSecond);

        LoginBodyModel loginData =
                new LoginBodyModel(registrationData.username(), registrationData.password());
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        LoginBodyModel loginDataSecond =new LoginBodyModel(usernameSecond, passwordSecond);
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

        ForbiddenDeleteReviewsResponseModel deleteReviewsResponse =
                ReviewsApiClient.forbiddenDeleteReviews(
                        accessTokenSecond,
                        createReviewsResponse.id());

        assertThat(deleteReviewsResponse.detail()).isEqualTo(FORBIDDEN_ERROR);
    }
}

package tests.UI.reviews;

import api.UsersApiClient;
import components.AuthComponent;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Просмотр отзыва на клуб")
public class ViewReviewsTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String accessToken;
    AuthComponent auth;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;
    String review;
    int assessment;
    int readPages;

    ViewClubPage viewClubPage = new ViewClubPage();

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.nanoTime();
        password = "pass_" + System.nanoTime();

        auth = new AuthComponent(api);
        accessToken = auth.setupAuthenticatedUser(username, password);

        long uniqueSuffix = System.nanoTime();
        bookTitle = faker.book().title() + "_" + System.nanoTime();
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
    @DisplayName("Успешный просмотр отзыва на клуб")
    public void SuccessfulCreateReviewsOnClubTests() {
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

        open(baseUrl + "clubs/" + createReviewsResponse.club());

        viewClubPage
                .reviewTextPublishVisible(review)
                .readPagesPublishVisible(readPages)
                .starsPublishVisible(assessment);
    }
}

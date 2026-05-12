package tests.UI.reviews;

import api.UsersApiClient;
import components.AuthComponent;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Создание отзыва на клуб")
public class CreateReviewsTests extends TestBase {

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

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.nanoTime();
        password = "pass_" + System.nanoTime();

        auth = new AuthComponent(api);
        accessToken = auth.setupAuthenticatedUser(username, password);

        long uniqueSuffix = System.nanoTime();
        bookTitle = faker.book().title() + "_" + System.nanoTime();;
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
    @DisplayName("Поиск и открытие страницы просмотра клуба")
    public void SuccessfulCreateReviewsOnClubTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                Integer.parseInt(String.valueOf(publicationYear)),
                description,
                telegramChatLink);

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        open(baseUrl + "clubs/" + clubId);

        ViewClubPage viewClubPage = new ViewClubPage();

        viewClubPage
                .createReviewsButtonClick()
                .titleReviewsFormVisible()
                .assessmentInputSetValue(assessment)
                .readPagesInputSetValue(readPages)
                .reviewInputSetValue(review)
                .publishButtonClick();

        viewClubPage
                .reviewTextPublishVisible(review)
                .readPagesPublishVisible(readPages)
                .starsPublishVisible(assessment);
    }
}

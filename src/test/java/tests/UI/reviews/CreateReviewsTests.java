package tests.UI.reviews;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Создание отзыва на клуб")
public class CreateReviewsTests extends TestBase {

    private final Faker faker = new Faker();

    String accessToken;
    ViewClubPage viewClubPage = new ViewClubPage();
    private TestDataBuilder testData;
    private AuthComponent auth;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withSecondUser()
                .withBook()
                .withReview();

        auth = new AuthComponent(api);
        accessToken = auth.setupAuthenticatedUser(testData.getUsername(), testData.getPassword());
    }

    @AfterEach
    public void after() {
        if (accessToken != null) {
            UsersApiClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное создание отзыва на клуб")
    public void SuccessfulCreateReviewsOnClubTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                testData.getBookTitle(),
                testData.getBookAuthors(),
                Integer.parseInt(String.valueOf(testData.getPublicationYear())),
                testData.getDescription(),
                testData.getTelegramChatLink());

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        open(baseUrl + "clubs/" + clubId);

        viewClubPage
                .createReviewsButtonClick()
                .titleReviewsFormVisible()
                .assessmentInputSetValue(testData.getAssessment())
                .readPagesInputSetValue(testData.getReadPages())
                .reviewInputSetValue(testData.getReview())
                .publishButtonClick();

        viewClubPage
                .reviewTextPublishVisible(testData.getReview())
                .readPagesPublishVisible(testData.getReadPages())
                .starsPublishVisible(testData.getAssessment());
    }
}

package tests.UI.reviews;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Редактирование отзыва на клуб")
public class EditReviewsTests extends TestBase {

    String accessToken;
    ViewClubPage viewClubPage = new ViewClubPage();
    private TestDataBuilder testData;
    private AuthComponent auth;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withBook()
                .withReview()
                .withUpdatedReview();

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
    @DisplayName("Успешное редактирование отзыва на клуб")
    public void SuccessfulCreateReviewsOnClubTests() {
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

        open(baseUrl + "clubs/" + createReviewsResponse.club());

        viewClubPage
                .editReviewButtonClick()
                .titleReviewsEditFormVisible()
                .assessmentInputSetValue(testData.getUpdatedAssessment())
                .readPagesInputSetValue(testData.getUpdateReadPages())
                .reviewInputSetValue(testData.getUpdatedReview())
                .publishButtonClick();

        viewClubPage
                .reviewTextPublishVisible(testData.getUpdatedReview())
                .readPagesPublishVisible(testData.getUpdateReadPages())
                .starsPublishVisible(testData.getUpdatedAssessment());
    }
}

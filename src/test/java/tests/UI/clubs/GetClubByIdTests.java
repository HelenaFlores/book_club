package tests.UI.clubs;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.TestDataBuilder;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Просмотр клуба")
public class GetClubByIdTests extends TestBase {

    String accessToken;
    HomePage homePage = new HomePage();
    ViewClubPage viewClubPage = new ViewClubPage();
    private TestDataBuilder testData;
    private AuthComponent auth;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withBook();

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
    @DisplayName("Поиск и открытие страницы просмотра клуба")
    public void OpenViewClubPageTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                testData.getBookTitle(),
                testData.getBookAuthors(),
                Integer.parseInt(String.valueOf(testData.getPublicationYear())),
                testData.getDescription(),
                testData.getTelegramChatLink());

        api.clubs.createClub(accessToken, clubData);

        open(baseUrl);

        homePage
                .searchByTitle(testData.getBookTitle())
                .clubShouldBeVisibleInList(testData.getBookTitle())
                .openClubByTitle();

        viewClubPage
                .bookTitleCardVisible(testData.getBookTitle());
    }

    @Test
    @DisplayName("Просмотр страницы клуба")
    public void DetailViewClubPageTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                testData.getBookTitle(),
                testData.getBookAuthors(),
                Integer.parseInt(String.valueOf(testData.getPublicationYear())),
                testData.getDescription(),
                testData.getTelegramChatLink());

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        SuccessfulCreateClubResponseModel apiClub = api.clubs.getClubById(accessToken, clubId);

        open(baseUrl + "clubs/" + clubId);

        viewClubPage
                .bookTitleCardVisible(testData.getBookTitle())
                .publicationYearCardVisible(testData.getPublicationYear())
                .bookAuthorsCardVisible(testData.getBookAuthors())
                .descriptionCardVisible(testData.getDescription())
                .telegramLinkVisible(testData.getTelegramChatLink())
                .membersCountShouldBe(apiClub.members().size())
                .reviewsCountShouldBe(apiClub.reviews().size())
                .leaveButtonVisible()
                .joinButtonNotVisible();
    }
}

package tests.UI.clubs;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.ClubHelper;
import helpers.TestDataBuilder;
import models.clubs.get.ClubItemModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CreateClubPage;
import pages.HomePage;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Создание клуба")
public class CreateClubTests extends TestBase {

    String accessToken;
    HomePage homePage = new HomePage();
    CreateClubPage createClubPage = new CreateClubPage();
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
    @DisplayName("Открытие страницы создания клуба")
    public void OpenCreateClubPageTests() {
        open(baseUrl);

        homePage.openCreateClubPage();

        createClubPage
                .createClubButtonVisible()
                .bookTitleInputVisible()
                .bookAuthorsInputVisible()
                .publicationYearInputVisible()
                .descriptionInputVisible()
                .telegramChatLinkInputVisible();
    }

    @Test
    @DisplayName("Успешное создание клуба")
    public void SuccessfulCreateClubTests() {
        open(baseUrl);

        homePage.openCreateClubPage();

        createClubPage
                .setBookTitleInput(testData.getBookTitle())
                .setBookAuthorsInput(testData.getBookAuthors())
                .setPublicationYearInput(testData.getPublicationYear())
                .setDescriptionInput(testData.getDescription())
                .setTelegramChatLinkInput(testData.getTelegramChatLink())
                .createClubButtonClick();

        api.clubs.getClubList(accessToken);
        ClubItemModel ourClub = ClubHelper.waitForClubInList(accessToken, testData.getBookTitle(), api.clubs, 10);
        int clubId = ourClub.id();

        open(baseUrl + "clubs/" + clubId);
        viewClubPage
                .bookTitleCardVisible(testData.getBookTitle())
                .publicationYearCardVisible(testData.getPublicationYear())
                .bookAuthorsCardVisible(testData.getBookAuthors())
                .descriptionCardVisible(testData.getDescription())
                .telegramLinkVisible(testData.getTelegramChatLink());
    }
}

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
import static tests.TestData.LEAVE_CLUB_ERROR;

@DisplayName("[UI] Выход из клуба")
public class LeaveClubByIdTests extends TestBase {

    String accessToken;
    String accessTokenSecond;
    HomePage homePage = new HomePage();
    ViewClubPage viewClubPage = new ViewClubPage();
    private TestDataBuilder testData;
    private AuthComponent auth;

    @BeforeEach
    public void prepareTestData() {
        testData = new TestDataBuilder()
                .withUser()
                .withSecondUser()
                .withBook();

        auth = new AuthComponent(api);
        accessToken = auth.setupAuthenticatedUser(testData.getUsername(), testData.getPassword());
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
    @DisplayName("Успешный выход из клуба")
    public void SuccessfulLeaveClubPageTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                testData.getBookTitle(),
                testData.getBookAuthors(),
                Integer.parseInt(String.valueOf(testData.getPublicationYear())),
                testData.getDescription(),
                testData.getTelegramChatLink());

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        accessTokenSecond = auth.setupAuthenticatedUser(testData.getUsernameSecond(), testData.getPasswordSecond());

        open(baseUrl + "clubs/" + clubId);

        viewClubPage
                .joinButtonClick()
                .leaveButtonClick();

        homePage
                .searchByTitle(testData.getBookTitle())
                .clubShouldBeVisibleInList(testData.getBookTitle());
        viewClubPage.joinButtonVisible();
    }

    @Test
    @DisplayName("Ошибка выхода из клуба")
    public void LeaveClubErrorTests() {
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
                .leaveButtonClick();

        viewClubPage.errorLeave(LEAVE_CLUB_ERROR);
    }
}

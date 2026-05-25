package tests.UI.clubs;

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
import static tests.TestData.LEAVE_CLUB_ERROR;

@DisplayName("[UI] Выход из клуба")
public class LeaveClubByIdTests extends TestBase {

    private final Faker faker = new Faker();

    String username;
    String password;
    String usernameSecond;
    String passwordSecond;
    String accessToken;
    String accessTokenSecond;
    AuthComponent auth;
    String bookTitle;
    String bookAuthors;
    int publicationYear;
    String description;
    String telegramChatLink;

    HomePage homePage = new HomePage();
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
                bookTitle,
                bookAuthors,
                Integer.parseInt(String.valueOf(publicationYear)),
                description,
                telegramChatLink);

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        usernameSecond = "user_" + System.nanoTime();
        passwordSecond = "pass_" + System.nanoTime();

        accessTokenSecond = auth.setupAuthenticatedUser(usernameSecond, passwordSecond);

        open(baseUrl + "clubs/" + clubId);

        viewClubPage
                .joinButtonClick()
                .leaveButtonClick();

        homePage
                .searchByTitle(bookTitle)
                .clubShouldBeVisibleInList(bookTitle);
        viewClubPage.joinButtonVisible();
    }

    @Test
    @DisplayName("Ошибка выхода из клуба")
    public void LeaveClubErrorTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                Integer.parseInt(String.valueOf(publicationYear)),
                description,
                telegramChatLink);

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        open(baseUrl + "clubs/" + clubId);

        viewClubPage
                .leaveButtonClick();

        viewClubPage.errorLeave(LEAVE_CLUB_ERROR);
    }
}

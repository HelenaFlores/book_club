package tests.UI.clubs;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.ClubHelper;
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

@DisplayName("[UI] Просмотр клуба")
public class GetClubByIdTests extends TestBase {

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
                bookTitle,
                bookAuthors,
                Integer.parseInt(String.valueOf(publicationYear)),
                description,
                telegramChatLink);
        api.clubs.createClub(accessToken, clubData);

        open(baseUrl);

        HomePage homePage = new HomePage();
        ViewClubPage viewClubPage = new ViewClubPage();

        homePage
                .searchByTitle(bookTitle)
                .clubShouldBeVisibleInList(bookTitle)
                .openClubByTitle();

        viewClubPage
                .bookTitleCardVisible(bookTitle);
    }

    @Test
    @DisplayName("Просмотр страницы клуба")
    public void DetailViewClubPageTests() {
        CreateClubBodyModel clubData = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                Integer.parseInt(String.valueOf(publicationYear)),
                description,
                telegramChatLink);

        SuccessfulCreateClubResponseModel createdClub = api.clubs.createClub(accessToken, clubData);
        int clubId = createdClub.id();

        SuccessfulCreateClubResponseModel apiClub = api.clubs.getClubById(accessToken, clubId);
        ViewClubPage viewClubPage = new ViewClubPage();

        open(baseUrl + "clubs/" + clubId);

        viewClubPage
                .bookTitleCardVisible(bookTitle)
                .publicationYearCardVisible(publicationYear)
                .bookAuthorsCardVisible(bookAuthors)
                .descriptionCardVisible(description)
                .telegramLinkVisible(telegramChatLink)
                .membersCountShouldBe(apiClub.members().size())
                .reviewsCountShouldBe(apiClub.reviews().size())
                .leaveButtonVisible()
                .joinButtonNotVisible();
    }
}

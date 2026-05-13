package tests.UI.clubs;

import api.UsersApiClient;
import components.AuthComponent;
import helpers.ClubHelper;
import models.clubs.get.ClubItemModel;
import models.clubs.get.SuccessfulGetClubListResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CreateClubPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ViewClubPage;
import tests.UI.TestBase;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("[UI] Создание клуба")
public class CreateClubTests extends TestBase {

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
    @DisplayName("Открытие страницы создания клуба")
    public void OpenCreateClubPageTests() {
        HomePage homePage = new HomePage();
        CreateClubPage createClubPage = new CreateClubPage();

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
        HomePage homePage = new HomePage();
        CreateClubPage createClubPage = new CreateClubPage();
        ViewClubPage viewClubPage = new ViewClubPage();

        open(baseUrl);

        homePage.openCreateClubPage();

        createClubPage
                .setBookTitleInput(bookTitle)
                .setBookAuthorsInput(bookAuthors)
                .setPublicationYearInput(publicationYear)
                .setDescriptionInput(description)
                .setTelegramChatLinkInput(telegramChatLink)
                .createClubButtonClick();

        SuccessfulGetClubListResponseModel clubListResponse = api.clubs.getClubList(accessToken);
        ClubItemModel ourClub = ClubHelper.waitForClubInList(accessToken, bookTitle, api.clubs, 10);
        int clubId = ourClub.id();

        open(baseUrl + "clubs/" + clubId);
        viewClubPage
                .bookTitleCardVisible(bookTitle)
                .publicationYearCardVisible(publicationYear)
                .bookAuthorsCardVisible(bookAuthors)
                .descriptionCardVisible(description)
                .telegramLinkVisible(telegramChatLink);
    }
}

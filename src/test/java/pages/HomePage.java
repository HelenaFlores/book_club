package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HomePage {

    private final SelenideElement registrationButton = $("a[href='/signup']");
    private final SelenideElement loginButton = $("a[href='/signin']");
    private final SelenideElement profileButton = $("a[href='/profile']");
    private final SelenideElement clubsEmptyResults = $(".no-results");
    private final SelenideElement createClubButton = $("a[href='/clubs/create']");
    private final SelenideElement searchInput = $(".search-input");
    private final SelenideElement openClubCardButton = $(".open-btn");
    private final ElementsCollection listClubCard = $$(".clubs-list");


    @Step("Открыть страницу регистрации")
    public HomePage openRegistrationPage() {
        registrationButton.click();
        return this;
    }

    @Step("Открыть страницу авторизации")
    public HomePage openLoginPage() {
        loginButton.click();
        return this;
    }

    @Step("Проверка видимости кнопки Профиль")
    public HomePage profileButtonVisible() {
        profileButton.shouldHave();
        return this;
    }

    @Step("Проверка отсутствия клубов")
    public HomePage clubsEmptyVisible() {
        clubsEmptyResults.shouldHave();
        return this;
    }

    @Step("Открыть страницу создания клуба")
    public HomePage openCreateClubPage() {
        createClubButton.click();
        return this;
    }

    @Step("Поиск книги по названию: {bookTitle}")
    public HomePage searchByTitle(String bookTitle) {
        searchInput.setValue(bookTitle).pressEnter();
        return this;
    }

    @Step("Проверка: клуб '{bookTitle}' отображается в результатах")
    public HomePage clubShouldBeVisibleInList(String bookTitle) {
        listClubCard.findBy(text(bookTitle)).shouldBe(visible);
        return this;
    }

    @Step("Открыть клуб")
    public HomePage openClubByTitle() {
        openClubCardButton.click();
        return this;
    }
}

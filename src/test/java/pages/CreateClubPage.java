package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CreateClubPage {

    private SelenideElement createClubButton = $(".submit-btn");
    private SelenideElement bookTitleInput = $("[id='bookTitle']");
    private SelenideElement bookAuthorsInput = $("[id='bookAuthors']");
    private SelenideElement publicationYearInput = $("[id='publicationYear']");
    private SelenideElement descriptionInput = $("[id='description']");
    private SelenideElement telegramChatLinkInput = $("[id='telegramChatLink']");

    @Step("Проверка видимости кнопки Создать клуб")
    public CreateClubPage createClubButtonVisible() {
        createClubButton.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля заголовка книги")
    public CreateClubPage bookTitleInputVisible() {
        bookTitleInput.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля автора книги")
    public CreateClubPage bookAuthorsInputVisible() {
        bookAuthorsInput.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля года выпуска книги")
    public CreateClubPage publicationYearInputVisible() {
        publicationYearInput.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля описания книги")
    public CreateClubPage descriptionInputVisible() {
        descriptionInput.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля ссылки на чат книги")
    public CreateClubPage telegramChatLinkInputVisible() {
        telegramChatLinkInput.shouldHave();
        return this;
    }

    @Step("Заполнение поля заголовка книги")
    public CreateClubPage setBookTitleInput(String bookTitle) {
        bookTitleInput.setValue(bookTitle);
        return this;
    }

    @Step("Заполнение поля автора книги")
    public CreateClubPage setBookAuthorsInput(String bookAuthors) {
        bookAuthorsInput.setValue(bookAuthors);
        return this;
    }

    @Step("Заполнение поля года выпуска книги")
    public CreateClubPage setPublicationYearInput(int publicationYear) {
        publicationYearInput.setValue(String.valueOf(publicationYear));
        return this;
    }

    @Step("Заполнение поля описания книги")
    public CreateClubPage setDescriptionInput(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Заполнение поля ссылки на чат книги")
    public CreateClubPage setTelegramChatLinkInput(String telegramChatLink) {
        telegramChatLinkInput.setValue(telegramChatLink);
        return this;
    }

    @Step("Клик кнопки Создать клуб")
    public CreateClubPage createClubButtonClick() {
        createClubButton.click();
        return this;
    }
}

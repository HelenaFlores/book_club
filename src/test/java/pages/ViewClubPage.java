package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ViewClubPage {

    private SelenideElement bookTitleCard = $(".club-header h1");
    private SelenideElement publicationYearCard = $(".year");
    private SelenideElement bookAuthorsCard = $(".authors");
    private SelenideElement descriptionCard = $(".description");
    private SelenideElement telegramLink = $(".telegram-btn");
    private SelenideElement membersCount =
            $$(".stat-item").get(0).$(".stat-value");
    private SelenideElement reviewsCount =
            $$(".stat-item").get(1).$(".stat-value");
    private SelenideElement leaveButton = $(".leave-btn");
    private SelenideElement joinButton = $(".join-btn");
    private SelenideElement errorLeaveText = $(".error");
    private SelenideElement createReviewsButton = $(".add-review-btn");
    private SelenideElement titleReviewsForm = $(".review-form h3");
    private SelenideElement assessmentInput = $("[id='assessment']");
    private SelenideElement readPagesInput = $("[id='readPages']");
    private SelenideElement reviewInput = $("[id='review']");
    private SelenideElement publishButton = $(".save-btn");
    private SelenideElement reviewTextPublish = $(".review-content p");
    private SelenideElement readPagesPublish = $(".read-pages");
    private SelenideElement starsPublish = $(".stars");



    @Step("Проверка заголовка книги: {bookTitle}")
    public ViewClubPage bookTitleCardVisible(String bookTitle) {
        bookTitleCard.shouldHave(text(bookTitle));
        return this;
    }

    @Step("Проверка заголовка книги: {publicationYear}")
    public ViewClubPage publicationYearCardVisible(int publicationYear) {
        publicationYearCard.shouldHave(exactText(String.valueOf(publicationYear)));
        return this;
    }

    @Step("Проверка заголовка книги: {bookAuthors}")
    public ViewClubPage bookAuthorsCardVisible(String bookAuthors) {
        bookAuthorsCard.shouldHave(text(bookAuthors));
        return this;
    }

    @Step("Проверка заголовка книги: {description}")
    public ViewClubPage descriptionCardVisible(String description) {
        descriptionCard.shouldHave(text(description));
        return this;
    }

    @Step("Проверка ссылки Telegram: {expectedLink}")
    public ViewClubPage telegramLinkVisible(String expectedLink) {
        telegramLink.shouldHave(attribute("href", expectedLink));
        return this;
    }

    @Step("Проверка количества участников: {count}")
    public ViewClubPage membersCountShouldBe(int count) {
        membersCount.shouldHave(exactText(String.valueOf(count)));
        return this;
    }

    @Step("Проверка количества отзывов: {count}")
    public ViewClubPage reviewsCountShouldBe(int count) {
        reviewsCount.shouldHave(exactText(String.valueOf(count)));
        return this;
    }

    @Step("Кнопка 'Покинуть клуб' видима")
    public ViewClubPage leaveButtonVisible() {
        leaveButton.shouldBe(visible);
        return this;
    }

    @Step("Кнопка 'Вступить в клуб' не видима")
    public ViewClubPage joinButtonNotVisible() {
        joinButton.shouldNot(exist);  // или shouldNotBe(visible)
        return this;
    }

    @Step("Кнопка 'Вступить в клуб' видима")
    public ViewClubPage joinButtonVisible() {
        joinButton.should(exist);
        return this;
    }

    @Step("Покинуть клуб")
    public ViewClubPage leaveButtonClick() {
        leaveButton.click();
        confirm();
        return this;
    }

    @Step("Ошибка выхода из своего клуба")
    public ViewClubPage errorLeave(String errorLeave) {
        errorLeaveText.shouldHave(exactText(String.valueOf(errorLeave)));
        return this;
    }

    @Step("Присоединиться к клубу")
    public ViewClubPage joinButtonClick() {
        joinButton.click();
        return this;
    }

    // создание отзыва
    @Step("Открыть форму заполнения отзыва")
    public ViewClubPage createReviewsButtonClick() {
        createReviewsButton.click();
        return this;
    }

    @Step("Проверка видимости заголовка формы отзыва")
    public ViewClubPage titleReviewsFormVisible() {
        titleReviewsForm.shouldHave();
        return this;
    }

    @Step("Заполнить поле оценки")
    public ViewClubPage assessmentInputSetValue(int assessment) {
        assessmentInput.setValue(String.valueOf(assessment));
        return this;
    }

    @Step("Заполнить поле количества прочитанных страниц")
    public ViewClubPage readPagesInputSetValue(int readPages) {
        readPagesInput.setValue(String.valueOf(readPages));
        return this;
    }

    @Step("Заполнить поле отзыва")
    public ViewClubPage reviewInputSetValue(String review) {
        reviewInput.setValue(review);
        return this;
    }

    @Step("Опубликовать отзыв")
    public ViewClubPage publishButtonClick() {
        publishButton.click();
        return this;
    }

    @Step("Проверка видимости текста созданного отзыва")
    public ViewClubPage reviewTextPublishVisible(String reviewText) {
        reviewTextPublish.shouldHave(exactText(String.valueOf(reviewText)));
        return this;
    }

    @Step("Проверка видимости количества прочитанных страниц созданного отзыва")
    public ViewClubPage readPagesPublishVisible(int readPages) {
        readPagesPublish.shouldHave(exactText(readPages + " стр."));
        return this;
    }

    @Step("Проверка видимости оценки созданного отзыва")
    public ViewClubPage starsPublishVisible(int stars) {
        String expected = "★".repeat(stars) + "☆".repeat(5 - stars);
        starsPublish.shouldHave(exactText(expected));
        return this;
    }
}

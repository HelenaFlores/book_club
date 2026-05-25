package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ViewClubPage {

    private final SelenideElement bookTitleCard = $(".club-header h1");
    private final SelenideElement publicationYearCard = $(".year");
    private final SelenideElement bookAuthorsCard = $(".authors");
    private final SelenideElement descriptionCard = $(".description");
    private final SelenideElement telegramLink = $(".telegram-btn");
    private final SelenideElement membersCount =
            $$(".stat-item").get(0).$(".stat-value");
    private final SelenideElement reviewsCount =
            $$(".stat-item").get(1).$(".stat-value");
    private final SelenideElement leaveButton = $(".leave-btn");
    private final SelenideElement joinButton = $(".join-btn");
    private final SelenideElement errorLeaveText = $(".error");
    private final SelenideElement createReviewsButton = $(".add-review-btn");
    private final SelenideElement titleReviewsForm = $(".review-form h3");
    private final SelenideElement assessmentInput = $("[id='assessment']");
    private final SelenideElement readPagesInput = $("[id='readPages']");
    private final SelenideElement reviewInput = $("[id='review']");
    private final SelenideElement publishButton = $(".save-btn");
    private final SelenideElement reviewTextPublish = $(".review-content p");
    private final SelenideElement readPagesPublish = $(".read-pages");
    private final SelenideElement starsPublish = $(".stars");
    private final SelenideElement editReviewButton = $(".edit-review-btn");
    private final SelenideElement titleReviewsEditForm = $(".review-form h3");
    private final SelenideElement deleteReviewButton = $(".delete-review-btn");


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

    @Step("Открыть форму редактирования отзыва")
    public ViewClubPage editReviewButtonClick() {
        editReviewButton.click();
        return this;
    }

    @Step("Проверка видимости заголовка формы редактирования отзыва")
    public ViewClubPage titleReviewsEditFormVisible() {
        titleReviewsEditForm.shouldHave();
        return this;
    }

    @Step("Удалить отзыв")
    public ViewClubPage deleteReviewButtonClick() {
        deleteReviewButton.click();
        confirm();
        return this;
    }

    @Step("Проверка наличия кнопки создания отзыва")
    public ViewClubPage createReviewsButtonVisible() {
        createReviewsButton.isEnabled();
        return this;
    }
}

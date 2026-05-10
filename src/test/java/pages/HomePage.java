package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class HomePage {

    private SelenideElement registrationButton = $("a[href='/signup']");
    private SelenideElement loginButton = $("a[href='/signin']");
    private SelenideElement profileButton = $("a[href='/profile']");
    private SelenideElement clubsEmptyResults = $(".no-results");


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
}

package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginButtonSubmit = $("[data-testid='submit-button']");
    private SelenideElement loginInput = $("[data-testid='username-input']");
    private SelenideElement passwordInput = $("[data-testid='password-input']");

    @Step("Проверка видимости кнопки Войти")
    public LoginPage loginButtonVisible() {
        loginButtonSubmit.isEnabled();
        return this;
    }

    @Step("Проверка видимости поля Логин")
    public LoginPage loginInputVisible() {
        loginInput.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля Пароль")
    public LoginPage passwordInputVisible() {
        passwordInput.shouldHave();
        return this;
    }

    @Step("Ввод логина")
    public LoginPage setLoginInput(String login) {
        loginInput.setValue(login);
        return this;
    }

    @Step("Ввод пароля")
    public LoginPage setPasswordInput(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Клик кнопки Войти")
    public LoginPage clickLoginButton() {
        loginButtonSubmit.click();
        return this;
    }
}

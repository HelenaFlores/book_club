package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {

    private SelenideElement registrationButtonSubmit = $("[data-testid='signup-button']");
    private SelenideElement loginInput = $("[data-testid='username-input']");
    private SelenideElement passwordInput = $("[data-testid='password-input']");
    private SelenideElement confirmpasswordInput = $("[data-testid='confirm-password-input']");


    @Step("Проверка видимости кнопки Зарегистрироваться")
    public RegistrationPage registrationButtonVisible() {
        registrationButtonSubmit.isEnabled();
        return this;
    }

    @Step("Проверка видимости поля Логин")
    public RegistrationPage loginInputVisible() {
        loginInput.shouldHave();
        return this;
    }

    @Step("Проверка видимости поля Пароль")
    public RegistrationPage passwordInputVisible() {
        passwordInput.shouldHave();
        return this;
    }

    @Step("Ввод логина")
    public RegistrationPage setLoginInput(String login) {
        loginInput.setValue(login);
        return this;
    }

    @Step("Ввод пароля")
    public RegistrationPage setPasswordInput(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Поавторный ввод пароля")
    public RegistrationPage setConfirmPasswordInput(String password) {
        confirmpasswordInput.setValue(password);
        return this;
    }

    @Step("Клик кнопки Зарегистрироваться")
    public RegistrationPage clickRegistrationButton() {
        registrationButtonSubmit.click();
        return this;
    }
}

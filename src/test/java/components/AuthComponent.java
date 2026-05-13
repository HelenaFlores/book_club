package components;

import api.ApiClient;
import helpers.JsonHelper;
import io.restassured.response.Response;
import models.AuthLocalStorageModel;
import models.clubs.get.ClubItemModel;
import models.clubs.get.SuccessfulGetClubListResponseModel;
import models.users.login.LoginBodyModel;
import models.users.registration.RegistrationBodyModel;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.users.login.LoginSpec.loginRequestSpec;
import static specs.users.login.LoginSpec.successfulLoginResponseSpec;
import static tests.TestData.MIN_URI_FOR_API;

public class AuthComponent {

    private final ApiClient api;
    private String accessToken;
    private String refreshToken;
    private String userId;

    public AuthComponent(ApiClient api) {
        this.api = api;
    }

    public void registerViaApi(String username, String password) {
        step("Регистрация пользователя через API", () -> {
            RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
            var response = api.users.register(registrationData);
            userId = String.valueOf(response.id());
        });
    }

    public void loginViaApiAndSetCookies(String username, String password) {
        step("Авторизация через API и установка cookies", () -> {
            LoginBodyModel loginBody = new LoginBodyModel(username, password);

            Response authResponse = given()
                    .spec(loginRequestSpec)
                    .body(loginBody)
                    .when()
                    .post("/auth/token/")
                    .then()
                    .spec(successfulLoginResponseSpec)
                    .extract()
                    .response();

            accessToken = authResponse.path("access");
            refreshToken = authResponse.path("refresh");

            open(MIN_URI_FOR_API);
            getWebDriver().manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(5));

            var authData = new AuthLocalStorageModel(
                    new AuthLocalStorageModel.UserInfo(
                            Integer.parseInt(userId), username, "", "", "", ""
                    ),
                    accessToken,
                    refreshToken,
                    true
            );

            executeJavaScript(
                    "localStorage.setItem('book_club_auth', arguments[0]);",
                    JsonHelper.toJson(authData)
            );

            refresh();
        });
    }

    public String setupAuthenticatedUser(String username, String password) {
        registerViaApi(username, password);
        loginViaApiAndSetCookies(username, password);
        return accessToken;
    }

    public String loginAfterUiRegistration(String username, String password) {
        return step("Логин после UI-регистрации", () -> {
            sleep(1000);
            LoginBodyModel loginData = new LoginBodyModel(username, password);
            return api.auth.loginAndGetAccessToken(loginData);
        });
    }
}
package api;

import io.qameta.allure.Step;
import models.users.login.*;
import models.users.logout.LogoutBodyModel;
import models.users.logout.LogoutEmptyBodyModel;
import models.users.logout.WrongLogoutNoValidTokenResponseModel;
import models.users.logout.WrongLogoutWithoutTokenResponseModel;
import models.users.update.AllUpdateBodyModel;
import models.users.update.PartialUpdateBodyModel;
import models.users.update.SuccessfulUpdateResponseModel;
import models.users.update.WrongUpdateMethodAllowedResponseModel;

import static io.restassured.RestAssured.given;
import static specs.users.login.LoginSpec.*;
import static specs.users.logout.LogoutSpec.*;
import static specs.users.update.UpdateSpec.*;

public class AuthApiClient {

    @Step("Авторизация")
    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    @Step("Авторизация и получение рефреш токена")
    public static String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    @Step("Авторизация и получение access токена")
    public String loginAndGetAccessToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("access");
    }

    @Step("Авторизация с ошибочными данными логина или пароля")
    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Авторизация с пустым username")
    public WrongLoginNullUsernameResponseModel wrongLoginNullUsernameResponse(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongLoginNullUsernameResponseSpec)
                .extract()
                .as(WrongLoginNullUsernameResponseModel.class);
    }

    @Step("Авторизация с пустым password")
    public WrongLoginNullPasswordResponseModel wrongLoginNullPasswordResponse(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongLoginNullPasswordResponseSpec)
                .extract()
                .as(WrongLoginNullPasswordResponseModel.class);
    }
    @Step("Отправка запроса LOGOUT с валидным refresh токеном")
    public static void logout(LogoutBodyModel logoutBody) {
        given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Отправка запроса LOGOUT с невалидным refresh токеном")
    public WrongLogoutNoValidTokenResponseModel logoutNoValidToken(LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(errorNoValidNokenLogoutResponseSpec)
                .extract()
                .as(WrongLogoutNoValidTokenResponseModel.class);
    }

    @Step("Отправка запроса LOGOUT без refresh токена")
    public WrongLogoutWithoutTokenResponseModel logoutWithoutRefreshToken(LogoutEmptyBodyModel logoutBody) {
       return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(errorEmptyBodyLogoutResponseSpec)
                .extract()
                .as(WrongLogoutWithoutTokenResponseModel.class);
    }

    @Step("Отправка PUT запроса на успешное обновление пользователя")
    public static SuccessfulUpdateResponseModel putUpdate(String accessToken, AllUpdateBodyModel putUpdateBody) {
      return given(updateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putUpdateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulPutUpdateResponseSpec)
                .extract()
                .as(SuccessfulUpdateResponseModel.class);
    }

    @Step("Отправка PATCH запроса на успешное обновление пользователя")
    public static SuccessfulUpdateResponseModel putPartialUpdate(String accessToken, PartialUpdateBodyModel putUpdateBody) {
        return given(updateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putUpdateBody)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulPutUpdateResponseSpec)
                .extract()
                .as(SuccessfulUpdateResponseModel.class);
    }

    @Step("Отправка PUT запроса на обновление пользователя с неверным методом - post")
    public static WrongUpdateMethodAllowedResponseModel errorMethodAllowedPutUpdate(String accessToken, AllUpdateBodyModel putUpdateBody) {
        return given(updateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putUpdateBody)
                .when()
                .post("/users/me/")
                .then()
                .spec(errorMethodAllowedResponseSpec)
                .extract()
                .as(WrongUpdateMethodAllowedResponseModel.class);
    }

    @Step("Отправка PATCH запроса на обновление пользователя с неверным методом - post")
    public static WrongUpdateMethodAllowedResponseModel errorMethodAllowedPatchUpdate(String accessToken, PartialUpdateBodyModel putUpdateBody) {
        return given(updateRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(putUpdateBody)
                .when()
                .post("/users/me/")
                .then()
                .spec(errorMethodAllowedResponseSpec)
                .extract()
                .as(WrongUpdateMethodAllowedResponseModel.class);
    }
}

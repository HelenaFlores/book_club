package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.users.registration.*;

import static io.restassured.RestAssured.given;
import static specs.clubs.delete.DeleteClubSpec.deleteClubRequestSpec;
import static specs.clubs.delete.DeleteClubSpec.successfulDeleteClubResponseSpec;
import static specs.users.delete.DeleteUserSpec.deleteUserRequestSpec;
import static specs.users.delete.DeleteUserSpec.successfulDeleteUserResponseSpec;
import static specs.users.registration.RegistrationSpec.*;

public class UsersApiClient {

    @Step("Отправка REGISTER запроса на создание пользователя")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("Отправка REGISTER запроса на создание существующего пользователя")
    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    @Step("Отправка REGISTER запроса на создание пользователя без password")
    public WrongRegistrationWithoutPasswordResponseModel registerWithoutPassword(RegistrationBodyWithoutPasswordModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongRegistrationWithoutPasswordResponseSpec)
                .extract()
                .as(WrongRegistrationWithoutPasswordResponseModel.class);
    }

    @Step("Отправка REGISTER запроса на создание пользователя без username")
    public WrongRegistrationWithoutLoginResponseModel registerWithoutLogin(RegistrationBodyWithoutLoginModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongRegistrationWithoutLoginResponseSpec)
                .extract()
                .as(WrongRegistrationWithoutLoginResponseModel.class);
    }

    @Step("Отправка DELETE запроса на удаление пользователя")
    public static ValidatableResponse deleteUser(String accessToken) {
        return given(deleteUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("users/me/")
                .then()
                .spec(successfulDeleteUserResponseSpec);
    }
}
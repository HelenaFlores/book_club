package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.clubs.delete.DeleteClubBodyModel;
import models.clubs.update.SuccessfulUpdateClubResponseModel;
import models.clubs.update.UpdateClubBodyModel;

import static io.restassured.RestAssured.given;
import static specs.clubs.create.CreateClubSpec.createClubRequestSpec;
import static specs.clubs.create.CreateClubSpec.successfulCreateClubResponseSpec;
import static specs.clubs.delete.DeleteClubSpec.deleteClubRequestSpec;
import static specs.clubs.delete.DeleteClubSpec.successfulDeleteClubResponseSpec;
import static specs.clubs.getbyid.GetClubByIdSpec.getClubByIdRequestSpec;
import static specs.clubs.getbyid.GetClubByIdSpec.successfulGetClubByIdResponseSpec;
import static specs.clubs.update.UpdateClubSpec.successfulUpdateClubResponseSpec;
import static specs.clubs.update.UpdateClubSpec.updateClubRequestSpec;

public class ClubsApiClient {

    @Step("Отправка POST запроса на создание книжного клуба")
    public SuccessfulCreateClubResponseModel createClub(String accessToken, CreateClubBodyModel createClubBody) {
        return given(createClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(createClubBody)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(SuccessfulCreateClubResponseModel.class);
    }

    @Step("Отправка GET запроса на получение книжного клуба по id")
    public SuccessfulCreateClubResponseModel getClubById(String accessToken, int clubId) {
        return given(getClubByIdRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", clubId)
                .when()
                .get("/clubs/{id}")
                .then()
                .spec(successfulGetClubByIdResponseSpec)
                .extract()
                .as(SuccessfulCreateClubResponseModel.class);
    }

    @Step("Отправка PATCH запроса на редактирование книжного клуба")
    public SuccessfulUpdateClubResponseModel updateClub(String accessToken, int clubId,
                                                        UpdateClubBodyModel updateClubBody) {
        return given(updateClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", clubId)
                .body(updateClubBody)
                .when()
                .patch("/clubs/{id}/")
                .then()
                .spec(successfulUpdateClubResponseSpec)
                .extract()
                .as(SuccessfulUpdateClubResponseModel.class);
    }

    @Step("Отправка DELETE запроса на удаление книжного клуба")
    public static ValidatableResponse deleteClub(String accessToken, int clubId) {
        return given(deleteClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", clubId)
                .when()
                .delete("/clubs/{id}/")
                .then()
                .spec(successfulDeleteClubResponseSpec);
    }
}

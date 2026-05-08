package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.reviews.update.SuccessfulUpdateReviewsResponseModel;
import models.reviews.update.UpdateReviewsBodyModel;

import static io.restassured.RestAssured.given;
import static specs.clubs.delete.DeleteClubSpec.deleteClubRequestSpec;
import static specs.clubs.delete.DeleteClubSpec.successfulDeleteClubResponseSpec;
import static specs.reviews.create.CreateReviewsSpec.createReviewsRequestSpec;
import static specs.reviews.create.CreateReviewsSpec.successfulCreateReviewsResponseSpec;
import static specs.reviews.update.UpdateReviewsSpec.successfulUpdateReviewsResponseSpec;
import static specs.reviews.update.UpdateReviewsSpec.updateReviewsRequestSpec;
import static specs.reviews.get.GetReviewsByIdSpec.getReviewsByIdRequestSpec;
import static specs.reviews.get.GetReviewsByIdSpec.successfulGetReviewsByIdResponseSpec;

public class ReviewsApiClient {

    @Step("Отправка POST запроса на создание отзыва")
    public SuccessfulCreateReviewsResponseModel createReviews(String accessToken, CreateReviewsBodyModel createReviewsBody) {
        return given(createReviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(createReviewsBody)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(successfulCreateReviewsResponseSpec)
                .extract()
                .as(SuccessfulCreateReviewsResponseModel.class);
    }

    @Step("Отправка GET запроса на получение отзыва по id")
    public SuccessfulCreateReviewsResponseModel getReviewsById(String accessToken, int reviewsId) {
        return given(getReviewsByIdRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", reviewsId)
                .when()
                .get("/clubs/reviews/{id}")
                .then()
                .spec(successfulGetReviewsByIdResponseSpec)
                .extract()
                .as(SuccessfulCreateReviewsResponseModel.class);
    }

    @Step("Отправка PUT запроса на редактирование отзыва")
    public SuccessfulUpdateReviewsResponseModel updateReviews(String accessToken, int reviewsId,
                                                        UpdateReviewsBodyModel updateReviewsBody) {
        return given(updateReviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", reviewsId)
                .body(updateReviewsBody)
                .when()
                .put("/clubs/reviews/{id}/")
                .then()
                .spec(successfulUpdateReviewsResponseSpec)
                .extract()
                .as(SuccessfulUpdateReviewsResponseModel.class);
    }

    /////////////////////////
    @Step("Отправка DELETE запроса на удаление книжного клуба")
    public static ValidatableResponse deleteClub(String accessToken, int clubId) {
        return given(deleteClubRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", clubId)
                .when()
                .delete("/clubs/reviews/{id}/")
                .then()
                .spec(successfulDeleteClubResponseSpec);
    }
}

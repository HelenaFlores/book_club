package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.CreateReviewsWithoutAuthResponseModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;
import models.reviews.update.InvalidClubUpdateReviewsResponseModel;
import models.reviews.update.SuccessfulUpdateReviewsResponseModel;
import models.reviews.update.UpdateReviewsBodyModel;
import org.apache.commons.lang3.ObjectUtils;

import static io.restassured.RestAssured.given;
import static specs.reviews.create.CreateReviewsSpec.*;
import static specs.reviews.delete.DeleteReviewsSpec.deleteReviewsRequestSpec;
import static specs.reviews.delete.DeleteReviewsSpec.successfulDeleteReviewsResponseSpec;
import static specs.reviews.get.GetReviewsByIdSpec.getReviewsByIdRequestSpec;
import static specs.reviews.get.GetReviewsByIdSpec.successfulGetReviewsByIdResponseSpec;
import static specs.reviews.update.UpdateReviewsSpec.*;

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

    @Step("Отправка POST запроса на создание отзыва без авторизации")
    public CreateReviewsWithoutAuthResponseModel createReviewsWithoutAuth(CreateReviewsBodyModel createReviewsBody) {
        return given(createReviewsRequestSpec)
                .body(createReviewsBody)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(createReviewsWithoutAuthResponseSpec)
                .extract()
                .as(CreateReviewsWithoutAuthResponseModel.class);
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

    @Step("Успешная отправка PUT запроса на редактирование отзыва")
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

    @Step("Отправка PUT запроса на редактирование отзыва в чужом клубе")
    public InvalidClubUpdateReviewsResponseModel invalidClubUpdateReviews(String accessToken, int reviewsId,
                                                        UpdateReviewsBodyModel updateReviewsBody) {
        return given(updateReviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", reviewsId)
                .body(updateReviewsBody)
                .when()
                .put("/clubs/reviews/{id}/")
                .then()
                .spec(invalidClubUpdateReviewsResponseSpec)
                .extract()
                .as(InvalidClubUpdateReviewsResponseModel.class);
    }

    @Step("Отправка DELETE запроса на удаление отзыва")
    public static ValidatableResponse deleteReviews(String accessToken, int reviewsId) {
        return given(deleteReviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", reviewsId)
                .when()
                .delete("/clubs/reviews/{id}/")
                .then()
                .spec(successfulDeleteReviewsResponseSpec);
    }
}

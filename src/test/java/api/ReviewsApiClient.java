package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.clubs.create.CreateClubBodyModel;
import models.clubs.create.SuccessfulCreateClubResponseModel;
import models.clubs.update.SuccessfulUpdateClubResponseModel;
import models.clubs.update.UpdateClubBodyModel;
import models.reviews.create.CreateReviewsBodyModel;
import models.reviews.create.SuccessfulCreateReviewsResponseModel;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;
import static specs.clubs.create.CreateClubSpec.successfulCreateClubResponseSpec;
import static specs.clubs.delete.DeleteClubSpec.deleteClubRequestSpec;
import static specs.clubs.delete.DeleteClubSpec.successfulDeleteClubResponseSpec;
import static specs.clubs.getbyid.GetClubByIdSpec.getClubByIdRequestSpec;
import static specs.clubs.getbyid.GetClubByIdSpec.successfulGetClubByIdResponseSpec;
import static specs.clubs.update.UpdateClubSpec.successfulUpdateClubResponseSpec;
import static specs.clubs.update.UpdateClubSpec.updateClubRequestSpec;
import static specs.reviews.create.CreateReviewsSpec.createReviewsRequestSpec;
import static specs.reviews.create.CreateReviewsSpec.successfulCreateReviewsResponseSpec;

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
}

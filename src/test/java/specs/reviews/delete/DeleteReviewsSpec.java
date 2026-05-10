package specs.reviews.delete;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static specs.BaseSpec.baseRequestSpec;

public class DeleteReviewsSpec {

    public static RequestSpecification deleteReviewsRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulDeleteReviewsResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();
}

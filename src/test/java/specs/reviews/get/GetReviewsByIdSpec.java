package specs.reviews.get;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class GetReviewsByIdSpec {

    public static RequestSpecification getReviewsByIdRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulGetReviewsByIdResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/get/successful_get_reviews_by_id_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("user", notNullValue())
            .expectBody("review", notNullValue())
            .expectBody("assessment", notNullValue())
            .expectBody("readPages", notNullValue())
            .expectBody("created", notNullValue())
            .build();
}

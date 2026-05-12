package specs.clubs.get;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static specs.BaseSpec.baseRequestSpec;

public class GetClubListSpec {

    public static RequestSpecification getClubListRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulGetClubListResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/get/successful_get_club_list_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("count", greaterThanOrEqualTo(0))
            .expectBody("next", anyOf(nullValue(), notNullValue()))
            .expectBody("previous", anyOf(nullValue(), notNullValue()))
            .expectBody("results", notNullValue())
            .expectBody("results.size()", greaterThan(0))
            .expectBody("results[0].id", notNullValue())
            .expectBody("results[0].bookTitle", notNullValue())
            .expectBody("results[0].bookAuthors", notNullValue())
            .expectBody("results[0].publicationYear", notNullValue())
            .expectBody("results[0].description", notNullValue())
            .expectBody("results[0].telegramChatLink", notNullValue())
            .expectBody("results[0].owner", notNullValue())
            .expectBody("results[0].members", notNullValue())
            .expectBody("results[0].reviews", notNullValue())
            .expectBody("results[0].created", notNullValue())
            .expectBody("results[0].modified", anyOf(nullValue(), notNullValue()))
            .expectBody("results[0].reviews[0].id", anyOf(nullValue(), notNullValue()))
            .expectBody("results[0].reviews[0].user.id", anyOf(nullValue(), notNullValue()))
            .expectBody("results[0].reviews[0].user.username", anyOf(nullValue(), notNullValue()))
            .build();
}

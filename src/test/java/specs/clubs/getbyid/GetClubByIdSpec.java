package specs.clubs.getbyid;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class GetClubByIdSpec {

    public static RequestSpecification getClubByIdRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulGetClubByIdResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/getbyid/successful_get_club_by_id_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .expectBody("publicationYear", notNullValue())
            .expectBody("description", notNullValue())
            .expectBody("telegramChatLink", notNullValue())
            .expectBody("owner", notNullValue())
            .expectBody("members", notNullValue())
            .expectBody("reviews", notNullValue())
            .expectBody("created", notNullValue())
            .build();
}

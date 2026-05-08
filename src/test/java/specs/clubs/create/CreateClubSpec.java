package specs.clubs.create;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class CreateClubSpec {

    public static RequestSpecification createClubRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulCreateClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/create/successful_create_club_response_schema.json"))
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

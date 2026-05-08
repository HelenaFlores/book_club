package specs.users.update;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class UpdateSpec {

    public static RequestSpecification updateRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulPutUpdateResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/users/update/successful_update_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("firstName", notNullValue())
            .expectBody("lastName", notNullValue())
            .expectBody("email", notNullValue())
            .expectBody("remoteAddr", notNullValue())
            .build();

    public static ResponseSpecification errorMethodAllowedResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(405)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/users/update/wrong_put_update_method_allowed_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

}

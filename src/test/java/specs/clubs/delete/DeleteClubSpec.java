package specs.clubs.delete;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class DeleteClubSpec {

    public static RequestSpecification deleteClubRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulDeleteClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();
}

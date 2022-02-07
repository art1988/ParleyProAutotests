package tests.customer_params.at223;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RemovePostExecutionParam extends LoginBase
{
    @BeforeTest
    public void setup()
    {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri(getBaseUrl() + "/tenants/properties")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", getApiKey())
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    public void removePostExecutionParam()
    {
        logger.info("Removing postExecutionForLibertyMutual setting...");

        given().
                when().
                    delete("/" + getTenantId() + "/postExecutionForLibertyMutual")
                        .then().statusCode(200);

        logger.info("Checking that postExecutionForLibertyMutual setting was removed...");
        given().
                when().
                    get("/" + getTenantId()).
                        then().
                            body("$", not(allOf(hasEntry("key", "postExecutionForLibertyMutual"))));

        logger.info("postExecutionForLibertyMutual setting was successfully removed...");
    }
}

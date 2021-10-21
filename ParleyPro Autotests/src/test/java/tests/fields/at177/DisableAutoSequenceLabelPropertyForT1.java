package tests.fields.at177;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tests.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DisableAutoSequenceLabelPropertyForT1 extends LoginBase
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
    @Description("This test disables autoSequenceLabel setting for tenant T1(qa-autotests).")
    public void disableAutoSequenceLabel()
    {
        logger.info("Disabling autoSequenceLabel setting...");

        given().
                when().
                    delete("/" + getTenantId() + "/autoSequenceLabel")
                        .then().statusCode(200);

        logger.info("Checking that autoSequenceLabel setting was removed...");
        given().
                when().
                    get("/" + getTenantId()).
                        then().
                            body("$", not(allOf(hasEntry("key", "autoSequenceLabel"))));

        logger.info("autoSequenceLabel setting was successfully removed...");
    }
}

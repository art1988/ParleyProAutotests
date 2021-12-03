package tests.regression.at82;

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

public class DisableNamingConvention extends LoginBase
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
    @Description("This test disables CONTRACT_NAME_TEMPLATE setting.")
    public void disableNamingConvention()
    {
        logger.info("Disabling CONTRACT_NAME_TEMPLATE setting...");

        given().
                when().
                    delete("/" + getTenantId() + "/CONTRACT_NAME_TEMPLATE")
                        .then().statusCode(200);

        logger.info("Checking that CONTRACT_NAME_TEMPLATE setting was removed...");
        given().
                when().
                    get("/" + getTenantId()).
                        then().
                            body("$", not(allOf(hasEntry("key", "CONTRACT_NAME_TEMPLATE"))));

        logger.info("CONTRACT_NAME_TEMPLATE setting was successfully removed...");
    }
}

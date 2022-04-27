package tests.customer_params.at254;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.Cache;
import utils.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RemoveProperties
{
    private static Logger logger = Logger.getLogger(RemoveProperties.class);
    private LoginBase loginBase;

    @BeforeTest
    public void setup()
    {
        loginBase = Cache.getInstance().getCachedLoginBase();

        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri(loginBase.getBaseUrl() + "/tenants/properties")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", loginBase.getApiKey())
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    public void removeBothProps()
    {
        logger.info("Removing COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS property...");
        given().
                when().
                    delete("/" + loginBase.getTenantId() + "/COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS")
                        .then().statusCode(200);

        logger.info("Removing contractAmmendment property...");
        given().
                when().
                    delete("/" + loginBase.getTenantId() + "/contractAmmendment")
                        .then().statusCode(200);

        logger.info("Checking that both properties were removed...");
        given().
                when().
                    get("/" + loginBase.getTenantId()).
                        then().
                            body("$", not(allOf(hasEntry("key", "COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS"),
                                                     hasEntry("key", "contractAmmendment"))));
    }
}

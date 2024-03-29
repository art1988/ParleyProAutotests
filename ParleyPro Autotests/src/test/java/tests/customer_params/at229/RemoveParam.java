package tests.customer_params.at229;

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

public class RemoveParam
{
    private static Logger logger = Logger.getLogger(RemoveParam.class);
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
    public void removeСlauseLibraryParam()
    {
        logger.info("Removing clauseLibrary setting...");

        given().
                when().
                    delete("/" + loginBase.getTenantId() + "/clauseLibrary")
                        .then().statusCode(200);

        logger.info("Checking that clauseLibrary setting was removed...");
        given().
                when().
                    get("/" + loginBase.getTenantId()).
                        then().
                            body("$", not(allOf(hasEntry("key", "clauseLibrary"))));

        logger.info("clauseLibrary setting was successfully removed...");
    }
}

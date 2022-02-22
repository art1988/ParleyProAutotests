package tests.customer_params.at223;

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

public class RemovePostExecutionParam
{
    private static Logger logger = Logger.getLogger(RemovePostExecutionParam.class);
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
    public void removePostExecutionParam()
    {
        logger.info("Removing postExecutionForLibertyMutual setting...");

        given().
                when().
                    delete("/" + loginBase.getTenantId() + "/postExecutionForLibertyMutual")
                        .then().statusCode(200);

        logger.info("Checking that postExecutionForLibertyMutual setting was removed...");
        given().
                when().
                    get("/" + loginBase.getTenantId()).
                        then().
                            body("$", not(allOf(hasEntry("key", "postExecutionForLibertyMutual"))));

        logger.info("postExecutionForLibertyMutual setting was successfully removed...");
    }
}

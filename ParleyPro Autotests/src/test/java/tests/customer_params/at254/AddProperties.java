package tests.customer_params.at254;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tests.customer_params.at229.AddParam;
import utils.Cache;
import utils.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AddProperties
{
    private static Logger logger = Logger.getLogger(AddParam.class);
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

    @Test(priority = 1)
    public void addBackendPropertyCOMBINE_ACTIVE_AND_UNFINISHED()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS");
        requestParams.put("value", "1");
        requestParams.put("nameSpace", "BACKEND");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        logger.info("Enabling COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS property...");
        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        logger.info("Checking that COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS property was added...");
        given().
                when().
                    get("/" + loginBase.getTenantId()).
                        then().
                            body("$", hasItem(allOf(hasEntry("key", "COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS"))));

        logger.info("COMBINE_ACTIVE_AND_UNFINISHED_EXECUTED_CONTRACTS property was successfully added...");
    }

    @Test(priority = 2)
    public void addFrontendPropertyContractAmmendment()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "contractAmmendment");
        requestParams.put("value", "true");
        requestParams.put("nameSpace", "FRONTEND");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        logger.info("Enabling contractAmmendment property...");
        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        logger.info("Checking that contractAmmendment property was added...");
        given().
                when().
                    get("/" + loginBase.getTenantId()).
                        then().
                            body("$", hasItem(allOf(hasEntry("key", "contractAmmendment"))));

        logger.info("contractAmmendment property was successfully added...");
    }
}

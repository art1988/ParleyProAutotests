package tests.regression.at82;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EnableNamingConvention extends LoginBase
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
    @Description("This test enables CONTRACT_NAME_TEMPLATE setting.")
    public void enableNamingConvention()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "CONTRACT_NAME_TEMPLATE");
        requestParams.put("value", "<<contract.getCounterpartyCompanyName()>>3Q<<contract.getCategory()>> <<contract.getAutoSequence()>> <<contract.getFieldAsArrayList('Services') | first>>");
        requestParams.put("nameSpace", "BACKEND");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        logger.info("Enabling CONTRACT_NAME_TEMPLATE setting...");
        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        logger.info("Checking that CONTRACT_NAME_TEMPLATE setting was enabled...");
        given().
                when().
                    get("/" + getTenantId()).
                        then().
                            body("$", hasItem(allOf(hasEntry("key", "CONTRACT_NAME_TEMPLATE"))));

        logger.info("CONTRACT_NAME_TEMPLATE setting was successfully added...");
    }
}

package tests.fields.at177;

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

public class EnableAutoSequenceLabelPropertyForT1 extends LoginBase
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
    @Description("This test enables autoSequenceLabel setting for tenant T1(qa-autotests).")
    public void enableAutoSequenceLabel()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "autoSequenceLabel");
        requestParams.put("value", "Nurix Contract No.");
        requestParams.put("nameSpace", "MIXED");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        logger.info("Enabling autoSequenceLabel setting...");
        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        logger.info("Checking that autoSequenceLabel setting was enabled...");
        given().
                when().
                    get("/" + getTenantId()).
                then().
                    body("$", hasItem(allOf(hasEntry("key", "autoSequenceLabel"))));

        logger.info("autoSequenceLabel setting was successfully added...");
    }
}

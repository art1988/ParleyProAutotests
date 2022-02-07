package tests.customer_params.at223;

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

public class AddPostExecutionParam extends LoginBase
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
    public void addPostExecutionParam()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "postExecutionForLibertyMutual");
        requestParams.put("value", true);
        requestParams.put("nameSpace", "FRONTEND");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        logger.info("Enabling postExecutionForLibertyMutual setting...");
        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        logger.info("Checking that postExecutionForLibertyMutual setting was enabled...");
        given().
                when().
                    get("/" + getTenantId()).
                        then().
                            body("$", hasItem(allOf(hasEntry("key", "postExecutionForLibertyMutual"))));

        logger.info("postExecutionForLibertyMutual setting was successfully added...");
    }
}

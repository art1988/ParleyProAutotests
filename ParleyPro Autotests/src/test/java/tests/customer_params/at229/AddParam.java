package tests.customer_params.at229;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.Cache;
import utils.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AddParam
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

    @Test
    public void addСlauseLibraryParam()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "clauseLibrary");
        requestParams.put("value", "[{\"title\": \"This is clause title\",\"text\": \"This is clause text.\"}]");
        requestParams.put("nameSpace", "FRONTEND");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        logger.info("Enabling clauseLibrary setting...");
        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        logger.info("Checking that clauseLibrary setting was enabled...");
        given().
                when().
                    get("/" + loginBase.getTenantId()).
                        then().
                            body("$", hasItem(allOf(hasEntry("key", "clauseLibrary"))));

        logger.info("clauseLibrary setting was successfully added...");
    }
}

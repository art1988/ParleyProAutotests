package tests.popovers.at145;

import constants.Const;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

////////////////////////////////////
// This test NOT FINISHED YET !!! //
////////////////////////////////////
public class AddProperty
{
    private static Logger logger = Logger.getLogger(AddProperty.class);

    @BeforeTest
    public void setup()
    {
        /*
        RequestSpecification requestSpec = new RequestSpecBuilder()
                     .setBaseUri("https://master.api.parleypro.net/tenants/properties") // TODO: add support for rc and prod
                     .setContentType(ContentType.JSON)
                     .setAccept(ContentType.JSON)
                     .addHeader("x-api-key", Const.QA_TENANT_API_KEY) // TODO: same for api key
                     .build();

        RestAssured.requestSpecification = requestSpec;*/
    }

    @Test
    @Description("This test adds DISABLE_COUNTERPARTY_DOC_SHARE_ON_TEAM_UPLOAD property to qa-autotests tenant. Then GET's that property and validates result.")
    public void addProperty()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "DISABLE_COUNTERPARTY_DOC_SHARE_ON_TEAM_UPLOAD");
        requestParams.put("value", "1");
        requestParams.put("nameSpace", "BACKEND");

        JSONArray array = new JSONArray();
        array.add(requestParams);

        given().
                body(array).
                    when().
                        post().
                            then().assertThat().statusCode(200);

        given().
               when().
                     get("/c4bca821-b6a3-417d-938d-9eb3ebb5564c").
                          then().
                                body("$", hasItem(allOf(hasEntry("key", "DISABLE_COUNTERPARTY_DOC_SHARE_ON_TEAM_UPLOAD"))));

        logger.info("DISABLE_COUNTERPARTY_DOC_SHARE_ON_TEAM_UPLOAD property was added...");
    }
}

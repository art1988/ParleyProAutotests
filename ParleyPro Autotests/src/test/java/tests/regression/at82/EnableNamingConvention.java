package tests.regression.at82;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tests.LoginBase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EnableNamingConvention extends LoginBase
{
    @BeforeTest
    public void setup()
    {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://master.api.parleypro.net/tenants/properties")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", getApiKey())
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    public void enableNamingConvention()
    {
        JSONObject requestParams = new JSONObject();

        requestParams.put("key", "CONTRACT_NAME_TEMPLATE");
        requestParams.put("value", "<<contract.getCounterpartyCompanyName()>>3Q<<contract.getCategory()>> <<contract.getAutoSequence()>> <<contract.getFieldAsArrayList('Services') | first>>");
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
                    get("/" + getTenantId()).
                        then().
                            body("$", hasItem(allOf(hasEntry("key", "CONTRACT_NAME_TEMPLATE"))));

        logger.info("CONTRACT_NAME_TEMPLATE property was added...");
    }
}

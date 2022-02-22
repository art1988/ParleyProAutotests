package tests.regression.at142;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.Cache;
import utils.LoginBase;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class RestoreDepartmentValues
{
    private static Logger logger = Logger.getLogger(RestoreDepartmentValues.class);
    private LoginBase loginBase;


    @BeforeTest
    public void setup()
    {
        loginBase = Cache.getInstance().getCachedLoginBase();

        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri(loginBase.getBaseUrl() + "/api/v1/fields-config")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", loginBase.getApiKey())
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    @Test
    @Description("Restore department to default values(department1 and department2) via API call")
    public void restoreDepartmentValues() throws ParseException, IOException
    {
        JSONParser parser = new JSONParser();

        String filePath = "src/main/resources/defaultFields_master.json";
        if(LoginBase.isRc())
        {
            filePath = "src/main/resources/defaultFields_rc.json";
        }
        else if(LoginBase.isProd())
        {
            filePath = "src/main/resources/defaultFields_prod.json";
        }

        String contentOfFile = FileUtils.readFileToString(new File(filePath), "UTF-8");
        JSONObject json = (JSONObject) parser.parse(contentOfFile);

        logger.info("Restoring default department values...");
        given().
           body(json).
                    when().
                        put().
                            then().assertThat().statusCode(204);
    }
}

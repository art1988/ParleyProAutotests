package tests.regression.at170;

import com.codeborne.selenide.Selenide;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Integrations;
import utils.LoginBase;
import utils.Screenshoter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CleanSettingsAndValidate extends LoginBase
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

    @Test(priority = 1)
    public void cleanSettingsAndValidate()
    {
        String[] keysToDelete = { "BACKUP_DRIVE_OWNER_EMAIL",
                                  "BACKUP_FOLDER",
                                  "BACKUP_GOOGLE_DRIVE_DOCUMENT_DIRECTORY_TEMPLATE",
                                  "BACKUP_GOOGLE_DRIVE_USER_ID"
                                };

        for( int i = 0; i < keysToDelete.length; i++ )
        {
            logger.info("Removing " + keysToDelete[i]);
            given().
                    when().
                        delete("/" + getTenantId() + "/" + keysToDelete[i])
                            .then()
                                .statusCode(200);
        }

        logger.info("Checking that all keys were removed...");
            given().
                    when().
                        get("/" + getTenantId()).
                            then().
                                body("$", not(allOf(hasEntry("key", keysToDelete[0]), hasEntry("key", keysToDelete[1]),
                                                         hasEntry("key", keysToDelete[2]), hasEntry("key", keysToDelete[3]))));
    }

    @Test(priority = 2)
    public void checkThatFieldsAreEmptyOnUI()
    {
        Integrations integrationsTab = new DashboardPage().getSideBar().clickAdministration().clickIntegrationsTab();

        logger.info("Scroll down to Google Drive...");
        Selenide.executeJavaScript("$('.integrations-google-drive')[0].scrollIntoView({});");

        Assert.assertEquals(integrationsTab.getGoogleDriveOwnerEmail(), "", "Drive Owner Email field still have value but shouldn't !!!");
        Assert.assertEquals(integrationsTab.getGoogleDrivePPUser(), "", "ParleyPro User field still have value but shouldn't !!!");
        Assert.assertEquals(integrationsTab.getGoogleDriveBackupFolder(), "", "Backup Folder still have value but shouldn't !!!");
        Assert.assertEquals(integrationsTab.getGoogleDriveDDTemplate(), "", "Document Directory Template still have value but shouldn't !!!");

        Screenshoter.makeScreenshot();
    }
}

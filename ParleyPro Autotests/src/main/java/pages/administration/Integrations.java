package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class Integrations
{
    private SelenideElement googleDriveOwnerEmailField   = $("input[data-label='Drive Owner Email']");
    private SelenideElement googleDrivePPUserField       = $(".integrations-google-drive input[placeholder*=\"Select a user\"]");
    private SelenideElement googleDriveBackupFolderField = $(".integrations-google-drive input[data-label=\"Backup Folder\"]");
    private SelenideElement googleDriveDDTemplate        = $(".integrations-google-drive input[data-label=\"Document Directory Template\"]");
    private SelenideElement getGoogleDriveSaveButton     = $(".integrations-google-drive button");

    private static Logger logger = Logger.getLogger(Integrations.class);


    public Integrations()
    {
        $(".spinner").waitUntil(Condition.disappear, 40_000);

        $(".integrations-docusign").shouldBe(Condition.visible);
        $(".integrations-adobe-sign").shouldBe(Condition.visible);
        $(".integrations-salesforce").shouldBe(Condition.visible);
    }

    public void setGoogleDriveOwnerEmail(String email)
    {
        googleDriveOwnerEmailField.shouldBe(Condition.visible, Condition.enabled).sendKeys(email);
    }

    public String getGoogleDriveOwnerEmail()
    {
        return googleDriveOwnerEmailField.getValue();
    }

    public void setGoogleDrivePPUser(String user)
    {
        googleDrivePPUserField.shouldBe(Condition.visible, Condition.enabled).sendKeys(user);
        googleDrivePPUserField.pressEnter();
    }

    public String getGoogleDrivePPUser()
    {
        return googleDrivePPUserField.getValue();
    }

    public void setGoogleDriveBackupFolder(String backupFolder)
    {
        googleDriveBackupFolderField.shouldBe(Condition.visible, Condition.enabled).sendKeys(backupFolder);
    }

    public String getGoogleDriveBackupFolder()
    {
        return googleDriveBackupFolderField.getValue();
    }

    public void setGoogleDriveDDTemplate(String ddTemplate)
    {
        googleDriveDDTemplate.shouldBe(Condition.visible, Condition.enabled).sendKeys(ddTemplate);
    }

    public String getGoogleDriveDDTemplate()
    {
        return googleDriveDDTemplate.getValue();
    }

    public void googleDriveSaveSettings()
    {
        getGoogleDriveSaveButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("SAVE button was clicked");
    }
}

package tests.basics.at220;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ContractInformation;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddExecutedContractAndUploadDifferentTypes
{
    private static Logger logger = Logger.getLogger(AddExecutedContractAndUploadDifferentTypes.class);


    @Test
    public void addExecutedContract()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-220 dif types");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        File[] fileTypes = new File(System.getProperty("user.dir") + "/Documents/executedFileTypes").listFiles();

        for( int i = 0; i < fileTypes.length; i++ )
        {
            uploadAndCheck(fileTypes[i]);

            // do not del the last doc from contract - will be deleted by another test
            if(i == fileTypes.length - 1) break;

            // del doc
            new OpenedContract().clickDocumentActionsMenu(FilenameUtils.getBaseName(fileTypes[i].getName())).clickDelete().clickDelete();

            $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been deleted."));
            $(".notification-stack").should(Condition.disappear);
        }
    }

    private void uploadAndCheck(File file)
    {
        new AddDocuments().clickUploadExecutedDocuments(file);

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been successfully uploaded."));
        $(".notification-stack").should(Condition.disappear);

        checkPresenceOnPage(FilenameUtils.getExtension(file.getName()));
    }

    private void checkPresenceOnPage(String ext)
    {
        switch (ext)
        {
            case "docx":
            {
                logger.info("Checking that docx doc was added...");
                $(".document__body-content").shouldBe(Condition.visible);
                Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"SCHEDULE\")').length === 1"), "There's no 'SCHEDULE' text on page !!!");
                Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Travel and Related\")').length === 1"), "There's no 'Travel and Related' text on page !!!");
                Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"to bind such party\")').length === 1"), "There's no 'to bind such party' text on page !!!");
                break;
            }

            case "jpg":
            case "png":
            {
                logger.info("Checking that jpg/png doc was added...");
                SelenideElement img = $$("img").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);
                Assert.assertTrue(img.isImage(), "Looks like that image wasn't loaded !!!");
                break;
            }

            case "tif":
            {
                logger.info("Checking that tif doc was added...");
                $$("canvas").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);
                break;
            }

            case "pdf":
            {
                logger.info("Checking that pdf doc was added...");
                $$(".textLayer").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);
                Assert.assertTrue(Selenide.executeJavaScript("return $('div[class=\"textLayer\"] div:contains(\"SCHEDULE\")').length === 1"), "There's no 'SCHEDULE' text on page !!!");
                Assert.assertTrue(Selenide.executeJavaScript("return $('div[class=\"textLayer\"] div:contains(\"Travel and Related\")').length === 1"), "There's no 'Travel and Related' text on page !!!");
                break;
            }
        }

        Screenshoter.makeScreenshot();
    }
}

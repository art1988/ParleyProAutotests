package tests.integrations.at217;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractAndSign
{
    private static Logger logger = Logger.getLogger(AddContractAndSign.class);

    @Test
    public void addContractAndMoveToSign() throws InterruptedException
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-217: DocuSign basics CTR");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();


        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        new OpenedContract().switchDocumentToSign("AT-14", true).clickDocusign().clickSign();

        logger.info("Redirecting to https://appdemo.docusign.com... Wait until page is loaded...");
        $(".sign-documents-waiting").waitUntil(Condition.disappear, 60_000 * 2);

        SelenideElement nameInput  = $("input[data-qa='recipient-name']").shouldBe(Condition.visible, Condition.enabled),
                        emailInput = $("input[data-qa='recipient-email']").shouldBe(Condition.visible, Condition.enabled);

        logger.info("Set name...");
        nameInput.sendKeys("Arthur");
        Thread.sleep(500);
        $("div[data-popover='true']").shouldBe(Condition.visible);
        nameInput.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        nameInput.sendKeys(Keys.ENTER);

        logger.info("Set email...");
        Thread.sleep(500);
        emailInput.click();
        for( int i = 0; i < 50; i++ )
        {
            emailInput.sendKeys(Keys.BACK_SPACE);
        }
        Thread.sleep(500);
        emailInput.sendKeys("arthur.khasanov+docusign@parleypro.com");
        Thread.sleep(1_000);

        $("button[data-qa='add-fields-link']").click();
        logger.info("Yellow button NEXT was clicked...");

        SelenideElement signatureButton = $("button[data-qa='Signature']").shouldBe(Condition.visible, Condition.enabled);
        signatureButton.click();
        logger.info("Signature field has been clicked...");

        $("image").click(); // click by <image> tag by the center of image
        Thread.sleep(1_000);

        Screenshoter.makeScreenshot();

        $("button[data-qa='send-button']").click();
        logger.info("Yellow button SEND was clicked...");

        logger.info("Redirecting back to ParleyPro... Wait until status became SIGN...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));
    }
}

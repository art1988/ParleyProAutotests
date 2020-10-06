package tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class UploadContract
{
    private static Logger logger = Logger.getLogger(UploadContract.class);


    @Test
    public void uploadContract()
    {
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(false);

        inProgressContractsPage.selectContract("Contract lifecycle autotest");

        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.CONTRACT_LIFECYCLE_SAMPLE );

        logger.info("Assert upload notification...");
        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document pramata has been successfully uploaded."));

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"PRAMATA\")')");

        logger.info("Assert that we can see the first headline of uploaded document");
        String headline = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"PRAMATA\")').text()");
        Assert.assertEquals(headline.trim(),"PRAMATA CORPORATION");

        logger.info("Assert that both contract and document icons have Draft status");
        $$(".lifecycle__item.first").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        logger.info("Assert that both contract and document icons have green color");
        $$(".lifecycle__item.first").first().getCssValue("background-color").equals("#28af96;");
        $$(".lifecycle__item.first").last().getCssValue("background-color").equals("#28af96;");

        Screenshoter.makeScreenshot();
    }
}

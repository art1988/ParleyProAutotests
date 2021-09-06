package tests.regression.at141;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocAndChange
{
    private static Logger logger = Logger.getLogger(CreateContractUploadDocAndChange.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        ContractInformation contractInformation = new InProgressContractsPage(false).clickNewContractButton();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withLocale(Locale.US);
        String dynamicContractTitle = "AT-141 - changes not tracked_" + LocalDateTime.now().format(formatter);

        Cache.getInstance().setContractTitle(dynamicContractTitle);

        contractInformation.setContractTitle( Cache.getInstance().getCachedContractTitle() );
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.REGRESSION_DOC_AT141 );

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.exactText("Document dummyAT141 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 35_000);

        new ContractInNegotiation( Cache.getInstance().getCachedContractTitle() ).clickOk();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void makeChangesAndCheckRedlines() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        openedContract.clickByParagraph("This is dummy PDF")
                      .sendSpecificKeys(new CharSequence[]{Keys.HOME, "asdf", Keys.DELETE, Keys.DELETE, Keys.DELETE, Keys.DELETE})
                      .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion "));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        logger.info("Assert that 'asdf' was added and 'This' was deleted...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"dummy PDF\")').find(\"ins\").text()"),
                "asdf", "Looks like that 'asdf' wasn't added !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"dummy PDF\")').find(\"del\").text()"),
                "This", "Looks like that 'This' wasn't deleted !!!");

        Screenshoter.makeScreenshot();
    }
}

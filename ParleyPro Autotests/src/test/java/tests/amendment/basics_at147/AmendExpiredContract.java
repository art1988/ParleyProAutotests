package tests.amendment.basics_at147;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.CompleteSign;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AmendExpiredContract
{
    private static Logger logger = Logger.getLogger(AmendExpiredContract.class);
    private SoftAssert softAssert = new SoftAssert();

    @Test(priority = 1)
    public void amendExpiredContract()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Expired");

        new OpenedContract().clickContractActionsMenu().clickAmendContract().clickSave();
        new AddDocuments();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void createNewAmendmentForSigned()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Signed");

        new OpenedContract().clickContractActionsMenu().clickAmendContract().clickSave();
        AddDocuments addDocuments = new AddDocuments();

        logger.info("Checking B in the name...");
        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Signed-B", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Hover link icon and check 2 links and that one is linked as related...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 2"));

        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 2"));
        $$(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Amendment to:\nExecuted Signed", "Related to:\nExecuted Signed-A"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void signAmendment() throws FileNotFoundException
    {
        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("Executed Signed-A");

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToSign("AT-14").clickStart();
        $("#COMPLETE_MANUAL_DOCUMENT").waitUntil(Condition.visible, 10_000);
        openedContract.clickCompleteSign("AT-14").clickComplete();
    }

    @Test(priority = 4)
    public void createAmendmentToAmendment()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Signed-A");

        new OpenedContract().clickContractActionsMenu().clickAmendContract().clickSave();
        AddDocuments addDocuments = new AddDocuments();

        logger.info("Checking double A in the name...");
        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Signed-A-A", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Checking that link has number 3...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 3"));

        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);
    }
}

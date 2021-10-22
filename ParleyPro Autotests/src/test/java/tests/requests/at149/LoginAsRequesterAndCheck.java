package tests.requests.at149;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndCheck
{
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndCheck.class);

    @Test
    public void loginAsRequesterAndAddRequest() throws InterruptedException
    {
        // Logout first
        LoginPage loginPage = new DashboardPage().getSideBar().logout();

        // Login as REQUESTER
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();
        contractRequest.uploadCounterpartyDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        $(".upload-field__file-name").waitUntil(Condition.visible, 10_000).shouldHave(Condition.text("AT-14.docx"));

        contractRequest.setValueForDropdown("requestField", "v1");

        contractRequest.clickSubmitRequest();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Your contract request has been submitted."));

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Contract request");

        ContractInfo contractInfo = new ContractInfo(true);

        logger.info("Filling up all mandatory fields...");
        contractInfo.setCounterpartyOrganization("CounterpartyAT");
        contractInfo.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        logger.info("Checking that status was switched to NEGOTIATE...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        new OpenedContract().clickByParagraph("Paragraph 1")
                            .setText(" some new added text")
                            .selectInternal()
                            .clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 35_000);

        logger.info("Assert that only one discussion was created...");
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(1));

        Screenshoter.makeScreenshot();

        logger.info("Making queued post...");
        new OpenedContract().clickByDiscussionIcon("Paragraph 1").clickMakeQueued("some new");
        $$(".discussion2-post").last().find(".active_queued span").shouldBe(Condition.visible).shouldHave(Condition.text("queued"));

        Screenshoter.makeScreenshot();
    }

    @AfterMethod
    public void deleteContractRequest()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("Contract request");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();

        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Contract Contract request has been deleted."));

        // Logout as requester
        new DashboardPage().getSideBar().logout();
    }
}

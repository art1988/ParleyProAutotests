package tests.basics.at61;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class TerminateExecutedAndReinstate
{
    private SideBar sideBar;

    private String host = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(TerminateExecutedAndReinstate.class);


    @Test(priority = 1)
    @Description("This test terminates executed contract and check it's status.")
    public void terminateExecuted() throws InterruptedException
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-61: terminate me");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.setExpirationEmailTo(Const.PREDEFINED_INTERNAL_USER_1.getEmail());
        contractInfo.setNotification("1 day");
        contractInfo.setExpirationDate("Jan 17, 2024");
        contractInfo.clickSave();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("MANAGED"));

        new OpenedContract().clickContractActionsMenu().clickTerminateContract().clickTerminate();

        logger.info("Checking that TERMINATED label is visible and status is still MANAGED...");
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been terminated."));
        $(".contract-header__right .label").shouldBe(Condition.visible).shouldHave(Condition.exactText("TERMINATED"));
        $(".lifecycle").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED"));

        logger.info("Checking Stage on Executed contracts page...");
        sideBar.clickExecutedContracts(false);
        $(".contracts-list__cell-stage").shouldBe(Condition.visible).shouldHave(Condition.exactText("TERMINATED"));

        Screenshoter.makeScreenshot();

        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "AT-61: terminate me contract has been terminated"),
                "Email with subject: 'AT-61: terminate me contract has been terminated' was not found !!!");
    }

    @Test(priority = 2)
    @Description("This test opens previously terminated contract and make it reinstate.")
    public void checkReinstate()
    {
        sideBar.clickExecutedContracts(false).selectContract("AT-61: terminate me");

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickContractActionsMenu().clickReinstateContract().clickReinstate();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been reinstated."));
        $(".contract-header__right .label").should(Condition.disappear);

        AuditTrail auditTrail = openedContract.clickAuditTrail();

        $$(".timeline-title").shouldHave(CollectionCondition.size(5)); // 5 events in total
        List<String> allEvents = $$(".timeline-title").stream().map(SelenideElement::text).collect(Collectors.toList());

        Assert.assertTrue(allEvents.contains("Contract reinstated"), "There is no such event as 'Contract reinstated' !!!");
        auditTrail.clickOk();

        ExecutedContractsPage executedContractsPage = sideBar.clickExecutedContracts(false);
        logger.info("Check that status become Managed...");
        $(".contract-status").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED"));
    }
}

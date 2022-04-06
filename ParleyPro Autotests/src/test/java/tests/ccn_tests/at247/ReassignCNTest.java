package tests.ccn_tests.at247;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ReassignCNTest
{
    private SideBar sideBar;
    private OpenedContract openedContract;

    private static final String CONTRACT_NAME = "AT-247 // Counterparty side: reassign CN";
    private static Logger logger = Logger.getLogger(ReassignCNTest.class);


    @BeforeMethod
    public void addContractUploadDocSwitchToNegotiate()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(CONTRACT_NAME);
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator(Const.PREDEFINED_CCN.getEmail());
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();
        openedContract.switchDocumentToNegotiate("AT-14", "CounterpartyAT", false).clickNext(false).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
    }

    @Test
    public void reassignCNTest() throws InterruptedException
    {
        asCCNReassignChiefNegotiator();
        openContractAndCheckUsersIconsInDocHeader();
        checkEmail();
        loginAsNewChiefNegotiator();
    }

    @Step
    public void asCCNReassignChiefNegotiator() throws InterruptedException
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_CCN.getEmail());
        loginPage.setPassword(Const.PREDEFINED_CCN.getPassword());
        sideBar = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS}).getSideBar();

        openedContract = new OpenedContract();

        logger.info("Clear previous Chief Negotiator and set new...");
        openedContract.clickContractActionsMenu()
                      .clickReassignChiefNegotiator()
                      .clearChiefNegotiatorField()
                      .setChiefNegotiator(Const.PREDEFINED_CCN2.getEmail())
                      .clickReassign();

        logger.info("Check that Chief Negotiator column was updated...");
        $(".contract-item .contracts-list__chief-negotiator-cell").shouldBe(Condition.visible).shouldHave(Condition.text("P CCN AT 2 fn F CCN AT 2 ln"));
    }

    @Step
    public void openContractAndCheckUsersIconsInDocHeader()
    {
        sideBar.clickInProgressContracts(false).selectContract(CONTRACT_NAME);

        logger.info("Checking user icons in document header...");
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("PF", "CC", "AL"));

        $$(".header-users .user").findBy(Condition.exactText("PF")).hover();
        $(".contract-user__section .contract-user__status").shouldBe(Condition.visible).shouldHave(Condition.exactText("Chief Negotiator"));
        Screenshoter.makeScreenshot();

        Selenide.executeJavaScript("$('.rc-tooltip-inner').hide(); $('.rc-tooltip-inner').remove()");
        $$(".header-users .user").findBy(Condition.exactText("CC")).hover();
        $(".contract-user__section .contract-user__status").shouldBe(Condition.visible).shouldHave(Condition.exactText("Lead"));
        Screenshoter.makeScreenshot();
    }

    @Step
    public void checkEmail() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "[qa-autotests] CCN AT fn CCN AT ln shared contract " + "\"" + CONTRACT_NAME + "\"" + " with you"),
                "Email with subject: '[qa-autotests] CCN AT fn CCN AT ln shared contract with you' was not found !!!");
    }

    @Step
    public void loginAsNewChiefNegotiator()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_CCN2.getEmail());
        loginPage.setPassword(Const.PREDEFINED_CCN2.getPassword());
        sideBar = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS}).getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(CONTRACT_NAME);
        openedContract = new OpenedContract();

        logger.info("Check presence of paragraphs...");
        for(int pNum = 1; pNum <= 7; pNum++) $(withText("Paragraph " + pNum)).shouldBe(Condition.visible);

        logger.info("Checking user icons in document header...");
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("PF", "CC", "AL"));

        $$(".header-users .user").findBy(Condition.exactText("PF")).hover();
        $(".contract-user__section .contract-user__status").shouldBe(Condition.visible).shouldHave(Condition.exactText("Chief Negotiator"));
        Screenshoter.makeScreenshot();

        Selenide.executeJavaScript("$('.rc-tooltip-inner').hide(); $('.rc-tooltip-inner').remove()");
        $$(".header-users .user").findBy(Condition.exactText("CC")).hover();
        $(".contract-user__section .contract-user__status").shouldBe(Condition.visible).shouldHave(Condition.exactText("Lead"));
        Screenshoter.makeScreenshot();
    }

    @AfterMethod
    public void loginBackAsMyTeamCN()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();
    }
}

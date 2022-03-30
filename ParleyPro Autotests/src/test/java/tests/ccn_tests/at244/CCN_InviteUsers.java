package tests.ccn_tests.at244;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CCN_InviteUsers
{
    private static final String CONTRACT_NAME = "AT-244: CCN Invite Users And Resend Invite";

    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(CCN_InviteUsers.class);


    @BeforeMethod
    public void addContractAndUploadDoc() throws InterruptedException
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(CONTRACT_NAME);
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();

        openedContract.switchDocumentToNegotiate("AT-14", "CounterpartyAT", false)
                      .clickNext(false)
                      .setCounterpartyChiefNegotiator(Const.PREDEFINED_CCN.getEmail())
                      .clickStart();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
    }

    @Test
    public void ccn_InviteUsers() throws InterruptedException
    {
        asCCNInviteNewUser();
        getEmailDocumentReviewRequestRegisterNewUserAndLogin();
    }

    @Step
    public void asCCNInviteNewUser() throws InterruptedException
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_CCN.getEmail());
        loginPage.setPassword(Const.PREDEFINED_CCN.getPassword());
        sideBar = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS}).getSideBar();

        String uniqueTimestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        String newUsersEmail = "arthur.khasanov+at244_" + uniqueTimestamp + "@parleypro.com";

        openedContract = new OpenedContract();
        openedContract.clickSHARE("AT-14").addParticipant(newUsersEmail).clickSend();
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("CC", "Ar", "AL"));
        $$(".document__header-info .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("CC", "Ar", "AL"));

        sideBar.logout();
    }

    @Step
    public void getEmailDocumentReviewRequestRegisterNewUserAndLogin() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract \"AT-244: CCN Invite Users And Resend Invite\": document review request"),
                "Email with subject: Contract \"AT-244: CCN Invite Users And Resend Invite\": document review request was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("has requested you to review document");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLFromButton = bodyText.substring(start + 1, end);
        URLFromButton = URLFromButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Opening URL from REVIEW DOCUMENT button...");
        Selenide.open(URLFromButton);

        logger.info("Going to register new user...");
        CreateParleyProAccountPage createParleyProAccountPage = new CreateParleyProAccountPage();

        createParleyProAccountPage.setFirstName("Invited_by_CCN_FN");
        createParleyProAccountPage.setLastName("Invited_by_CCN_LN");
        createParleyProAccountPage.setPassword("Parley650!");
        createParleyProAccountPage.setConfirmPassword("Parley650!");
        createParleyProAccountPage.clickCreateAndSignIn();

        openedContract = new OpenedContract();
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("CC", "II", "AL"));
        $$(".document__header-info .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("CC", "II", "AL"));

        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("3RD PARTY"));

        logger.info("Check that all paragraphs are visible...");
        for( int i = 1; i <= 7; i++ ) $(withText("Paragraph " + i)).shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();

        logger.info("Login back as My Team CN...");
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();
    }
}

package tests.ccn_tests.invite_ccn;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import forms.ContractInformation;
import forms.StartNegotiation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class InviteCounterpartyChiefNegotiator
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";
    private String contractName = "Invite CCN";
    private String counterparty = "CounterpartyAT";
    private String uniqueTimestamp;

    private static Logger logger = Logger.getLogger(InviteCounterpartyChiefNegotiator.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        uniqueTimestamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        String uniqueEmailOfCCN = "arthur.khasanov+ccn_" + uniqueTimestamp + "@parleypro.com";

        // Create new contract
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle(contractName);
        contractInformationForm.setCounterpartyOrganization(counterparty);
        contractInformationForm.setCounterpartyChiefNegotiator(uniqueEmailOfCCN);
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_DOC_AT83_BDOC1 );

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Services Agreement.\")')");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void moveDocToNegotiate()
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate("bdoc1", counterparty, false);
        startNegotiationForm.clickNext(false).clickStart();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void getEmailAndOpenRegistrationPage() throws InterruptedException
    {
        logger.info("Waiting for 20 seconds to make sure that email has been delivered...");
        Thread.sleep(20_000);

        EmailChecker.assertEmailBySubject(host, username, password, "[qa-autotests] autotest_cn fn ln shared contract \"" + contractName + "\" with you");

        String bodyText = EmailChecker.assertEmailBodyText("Please click \"Get started\" below to review the contract.");

        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");

        String URLOfRegistrationPage = bodyText.substring(start + 1, end);
        URLOfRegistrationPage = URLOfRegistrationPage.replaceAll("amp;", ""); // replace amp; symbol

        Selenide.open(URLOfRegistrationPage);
    }

    @Test(priority = 4)
    public void createCCNUser()
    {
        CreateParleyProAccountPage createParleyProAccountPage = new CreateParleyProAccountPage();

        createParleyProAccountPage.setFirstName("CCN_Auto_FirstName_" + uniqueTimestamp);
        createParleyProAccountPage.setLastName("CCN_Auto_LastName_" + uniqueTimestamp);
        createParleyProAccountPage.setPassword("Parley650!");
        createParleyProAccountPage.setConfirmPassword("Parley650!");

        Screenshoter.makeScreenshot();

        logger.info("Login as CCN...");
        OpenedContract openedContract = createParleyProAccountPage.clickCreateAndSignIn();

        Assert.assertEquals(openedContract.getContractName(), "Invite CCN");

        $$(".header-users .user").shouldHave(CollectionCondition.size(2));
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(2));

        logger.info("Hover document's users...");
        $$(".header-users .user").get(0).hover();
        $(".rc-tooltip-inner .spinner").waitUntil(Condition.disappear, 8_000);
        $(".contract-user__name").waitUntil(Condition.visible, 5_000).
                    shouldHave(Condition.exactText("CCN_Auto_FirstName_" + uniqueTimestamp + " " + "CCN_Auto_LastName_" + uniqueTimestamp));
        $(".contract-user__status").waitUntil(Condition.visible, 5_000).
                    shouldHave(Condition.exactText("Chief Negotiator"));

        Screenshoter.makeScreenshot();

        $$(".header-users .user").get(1).hover();
        $(".rc-tooltip-inner .spinner").waitUntil(Condition.disappear, 8_000);
        $(".contract-user__name").waitUntil(Condition.visible, 5_000).
                shouldHave(Condition.exactText("autotest_cn fn ln"));
        $(".contract-user__status").waitUntil(Condition.visible, 5_000).
                shouldHave(Condition.exactText("Chief Negotiator"));

        new DashboardPage(new SideBarItems[]
                {
                    SideBarItems.IN_PROGRESS_CONTRACTS,
                    SideBarItems.EXECUTED_CONTRACTS,
                    SideBarItems.USER_GUIDE
                }).getSideBar().logout();
    }
}

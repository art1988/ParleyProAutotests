package tests.ccn_tests.invite_ccn;

import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.StartNegotiation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Waiter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Listeners({ScreenShotOnFailListener.class})
public class InviteCounterpartyChiefNegotiator
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";
    private String contractName = "Invite CCN";
    private String counterparty = "CounterpartyAT";

    private static Logger logger = Logger.getLogger(InviteCounterpartyChiefNegotiator.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        String uniqueEmailOfCCN = "arthur.khasanov+ccn_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + "@parleypro.com";

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
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.REGRESSION_DOC_AT83_BDOC1 );

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Services Agreement.\")')");
    }

    @Test(priority = 2)
    public void moveDocToNegotiate()
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate("bdoc1", counterparty, false);
        startNegotiationForm.clickNext(false).clickStart();
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

        Selenide.open(URLOfRegistrationPage);
    }
}

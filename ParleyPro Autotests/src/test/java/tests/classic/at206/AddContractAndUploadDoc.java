package tests.classic.at206;

import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.Cache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class AddContractAndUploadDoc
{
    @Test
    public void addContractAndUploadDoc()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(false).clickNewContractButton();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withLocale(Locale.US);
        String dynamicContractTitle = "AT-206 - LibertyMutual_Classic_" + LocalDateTime.now().format(formatter);
        Cache.getInstance().setContractTitle(dynamicContractTitle);

        contractInformationForm.setContractTitle( dynamicContractTitle );
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setCounterpartyOrganization("Applebook");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");

        contractInformationForm.clickSave();


        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadCounterpartyDocuments( Const.TRACK_CHANGES_CLASSIC_AT206 );

        new ContractInNegotiation(dynamicContractTitle).clickOk();
    }
}

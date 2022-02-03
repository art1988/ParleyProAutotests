package tests.basics.at224;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.ContractInformation;
import model.User;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractAndShare
{
    @Test(priority = 1)
    public void addContract()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-224: share me");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();


        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        // Save Contract id
        String curURL = WebDriverRunner.getWebDriver().getCurrentUrl();
        String contractId = curURL.substring(curURL.lastIndexOf("contracts/") + "contracts/".length(), curURL.indexOf("/docs"));
        Cache.getInstance().setContractId(contractId);
    }

    @Test(priority = 2)
    public void shareWithUser() throws InterruptedException
    {
        User myTeamUser = new User("AT-224 myTeamUser fn", "AT-224 myTeamUser ln", "arthur.khasanov+at224_myteamuser@parleypro.com", "Parley650!");
        Cache.getInstance().setUser(myTeamUser);

        new OpenedContract().clickSHARE("AT-14").addParticipant(myTeamUser.getEmail()).clickSend();

        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "Ar"));
    }

    @Test(priority = 3)
    public void logout()
    {
        new DashboardPage().getSideBar().logout();
    }
}

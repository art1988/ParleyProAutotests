package tests.contract_info.at187;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractUploadDoc
{
    private String contractTitle = "AT-187 CONTRACT";
    private static Logger logger = Logger.getLogger(AddContractUploadDoc.class);


    @Test(priority = 1)
    public void addContractUploadDoc()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        Cache.getInstance().setContractTitle(contractTitle);

        contractInformationForm.setContractTitle(contractTitle);
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments(Const.REGRESSION_DOC_AT141);
        new ContractInNegotiation(contractTitle).clickOk();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
    }

    @Test(priority = 2)
    public void senInvite() throws InterruptedException
    {
        String additionalCounterpartyUser = "arthur.khasanov+additional_ccn_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + "@parleypro.com";

        Cache.getInstance().setAdditionalCounterpartyUser(additionalCounterpartyUser);

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickSendInvite().clickNext(false)
                                        .setCounterpartyChiefNegotiator(Const.PREDEFINED_CCN.getEmail())
                                        .clickAddCounterpartyUsers()
                                        .setAdditionalCounterpartyUsers(additionalCounterpartyUser)
                                        .clickStart();

        logger.info("Assert that both CCN and Additional Counterparty user were saved...");
        ContractInformation contractInformation = openedContract.clickContractInfo();

        Assert.assertTrue($("#counterpartyChiefNegotiator").getValue().contains("arthur.khasanov+ccn_at@parleypro.com"), "Counterparty Chief Negotiator is missing !!!");
        Assert.assertEquals($$(".tags-input span").filter(Condition.exactText("Additional Counterparty users")).first().closest("div").find(".tags-input__tag").getText(),
                Cache.getInstance().getCachedAdditionalCounterpartyUser(), "Additional Counterparty user is missing !!!");
        Screenshoter.makeScreenshot();

        $$(".modal-footer button").filter(Condition.exactText("CANCEL")).first().click(); // close 'Contract Info' right panel
    }
}

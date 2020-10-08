package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InProgressContractsPage;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class AddNewContract
{
    private static Logger logger = Logger.getLogger(AddNewContract.class);


    @Test
    public void addNewContract()
    {
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract lifecycle autotest");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        Assert.assertEquals(contractInformationForm.getChiefNegotiator(), Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName() + " (" + Const.PREDEFINED_USER_CN_ROLE.getEmail() + ")");

        contractInformationForm.clickSave();

        inProgressContractsPage.getSideBar().clickInProgressContracts(false);

        logger.info("Assert that contract present in the list and 'Contract title', 'Chief Negotiator' and 'Stage' have correct values");

        Waiter.smartWaitUntilVisible("$('.contracts-list__contract-name')");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.contracts-list__contract-name').text()"), "Contract lifecycle autotest");

        $("a .contracts-list__chief-negotiator-cell").shouldHave(Condition.exactText(Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName()));
        $("a .contracts-list__cell-stage").shouldHave(Condition.exactText("Draft"));

        Screenshoter.makeScreenshot();
    }
}

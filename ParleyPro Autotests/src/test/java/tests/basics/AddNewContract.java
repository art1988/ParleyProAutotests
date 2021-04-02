package tests.basics;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddNewContract
{
    private static Logger logger = Logger.getLogger(AddNewContract.class);


    @Test
    public void addNewContract() throws InterruptedException
    {
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        /* ~~~~~~~~~~~~~   Setting values of Contract information form  ~~~~~~~~~~~~~~~~~~~~~~~~~ */
        contractInformationForm.setContractTitle("Contract lifecycle autotest");
        contractInformationForm.setDueDate();
        contractInformationForm.setContractValue("18000");
        contractInformationForm.setContractRadioButton("Other");
        contractInformationForm.setMyCompanyTemplate(false);
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+autotestcn@parleypro.com");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.setTag("Test");
        contractInformationForm.setTag("One more tag");
        contractInformationForm.setNotes("These notes were added from autotest. Check me.");

        contractInformationForm.clickSave();
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy").withLocale(Locale.US);

        logger.info("Open Contract Info and assert that all fields were saved...");
        contractInformationForm = new OpenedContract().clickContractInfo();

        Assert.assertEquals(contractInformationForm.getDueDate(), LocalDate.now().format(formatter));
        Assert.assertEquals(contractInformationForm.getContractValue(), "18,000.00");
        Assert.assertEquals(contractInformationForm.getContractRadioButtonSelection(), "Other");
        Assert.assertEquals(contractInformationForm.getMyCompanyTemplateRadioButtonSelection(), "No");
        Assert.assertEquals(contractInformationForm.getContractingRegion(), "region1");
        Assert.assertEquals(contractInformationForm.getContractingCountry(), "country1");
        Assert.assertEquals(contractInformationForm.getContractEntity(), "entity1");
        Assert.assertEquals(contractInformationForm.getContractingDepartment(), "department1");
        Assert.assertEquals(contractInformationForm.getContractCategory(), "category1");
        Assert.assertEquals(contractInformationForm.getContractType(), "type1");
        Assert.assertEquals(contractInformationForm.getChiefNegotiator(), Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName() + " (" + Const.PREDEFINED_USER_CN_ROLE.getEmail() + ")");
        contractInformationForm.getTags().shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Test", "One more tag"));
        Assert.assertEquals(contractInformationForm.getNotes(), "These notes were added from autotest. Check me.");

        contractInformationForm.clickCancel();

        inProgressContractsPage.getSideBar().clickInProgressContracts(false);

        logger.info("Assert that contract present in the list and 'Contract title', 'Chief Negotiator' and 'Stage' have correct values");

        Waiter.smartWaitUntilVisible("$('.contracts-list__contract-name')");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.contracts-list__contract-name').text()"), "Contract lifecycle autotest");

        $("a .contracts-list__chief-negotiator-cell").shouldHave(Condition.exactText(Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName()));
        $("a .contracts-list__cell-stage").shouldHave(Condition.exactText("Draft"));

        Screenshoter.makeScreenshot();
    }
}

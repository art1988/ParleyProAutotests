package tests.basics.at8;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.DashboardPage;
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
    private SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(AddNewContract.class);

    @Test
    @Step
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

        logger.info("Open [Contract Info] and assert that all fields were saved...");
        contractInformationForm = new OpenedContract().clickContractInfo();

        softAssert.assertEquals(contractInformationForm.getDueDate(), LocalDate.now().format(formatter));
        softAssert.assertEquals(contractInformationForm.getContractValue(), "18,000.00");
        softAssert.assertEquals(contractInformationForm.getContractRadioButtonSelection(), "Other");
        softAssert.assertEquals(contractInformationForm.getMyCompanyTemplateRadioButtonSelection(), "No");
        softAssert.assertEquals(contractInformationForm.getCounterpartyOrganization(), "CounterpartyAT");
        softAssert.assertEquals(contractInformationForm.getContractingRegion(), "region1");
        softAssert.assertEquals(contractInformationForm.getContractingCountry(), "country1");
        softAssert.assertEquals(contractInformationForm.getContractEntity(), "entity1");
        softAssert.assertEquals(contractInformationForm.getContractingDepartment(), "department1");
        softAssert.assertEquals(contractInformationForm.getContractCategory(), "category1");
        softAssert.assertEquals(contractInformationForm.getContractType(), "type1");
        softAssert.assertEquals(contractInformationForm.getChiefNegotiator(), Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName() + " (" + Const.PREDEFINED_USER_CN_ROLE.getEmail() + ")");
        contractInformationForm.getTags().shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Test", "One more tag"));
        softAssert.assertEquals(contractInformationForm.getNotes(), "These notes were added from autotest. Check me.");

        Screenshoter.makeScreenshot();
        contractInformationForm.clickCancel();

        new DashboardPage().getSideBar().clickInProgressContracts(false);

        logger.info("Assert that Contract is present in the list and 'Contract title', 'Chief Negotiator' and 'Stage' have correct values");

        Waiter.smartWaitUntilVisible("$('.contracts-list__contract-name')");
        Assert.assertEquals($(".contracts-list__contract-name").text(), "Contract lifecycle autotest");

        $("a .contracts-list__chief-negotiator-cell").shouldHave(Condition.exactText(Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName()));
        $("a .contracts-list__cell-stage").shouldHave(Condition.exactText("Draft"));

        logger.info("Perform assert of all fields on Contract Information form...");
        softAssert.assertAll();

        Screenshoter.makeScreenshot();
    }
}

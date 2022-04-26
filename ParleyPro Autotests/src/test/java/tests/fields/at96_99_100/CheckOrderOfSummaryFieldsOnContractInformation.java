package tests.fields.at96_99_100;

import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckOrderOfSummaryFieldsOnContractInformation
{
    private static Logger logger = Logger.getLogger(CheckOrderOfSummaryFieldsOnContractInformation.class);

    @Test(priority = 1)
    @Description("This test goes to In-Progress page, add new contract and checks fields order.")
    public void addNewContractAndCheckOrder()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        $(".spinner").should(disappear);

        // Scroll Contract information to bottom
        Selenide.executeJavaScript("$('.modal__scrollable-body').scrollTop($('.modal__scrollable-body')[0].scrollHeight);");

        logger.info("Check summary field order on Contract Information form...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('span:contains(\"f2\")').parent().parent().parent().parent().text()"),
                   "f2f1",
                   "The order of summary fields f1 and f2 is wrong !!!");

        Screenshoter.makeScreenshot();

        contractInformationForm.clickCancel();
    }
}

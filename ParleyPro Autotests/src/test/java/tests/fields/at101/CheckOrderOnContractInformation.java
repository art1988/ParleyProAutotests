package tests.fields.at101;

import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

@Listeners({ScreenShotOnFailListener.class})
public class CheckOrderOnContractInformation
{
    private static Logger logger = Logger.getLogger(CheckOrderOnContractInformation.class);

    @Test
    public void checkOrderOnContractInformation()
    {
        ContractInformation contractInformationFrom = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        // Scroll Contract information to bottom
        Selenide.executeJavaScript("$('.modal__scrollable-body').scrollTop($('.modal__scrollable-body')[0].scrollHeight);");

        logger.info("Check fields order on Contract Information form...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('span:contains(\"ff1\")').parent().parent().parent().parent().find('label').text()"),
                "linked2ff1ff2linked1",
                "The order of summary fields linked1, ff1, linked2 and ff2 is wrong !!!");

        Screenshoter.makeScreenshot();

        contractInformationFrom.clickCancel();
    }
}

package tests.contract_info.at169;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class DisableEnableAutoRenewalFillAgainAndCheck
{
    private ContractInfo contractInfo;
    private String renewalVal;
    private static Logger logger = Logger.getLogger(DisableEnableAutoRenewalFillAgainAndCheck.class);

    @Test(priority = 1)
    public void disableEnableAutoRenewalAndFillAgain() throws InterruptedException
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("AT-169 auto-renewal");

        contractInfo = new ContractInfo();

        logger.info("Disable auto-renewal...");
        contractInfo.clickAutoRenewalTumbler();
        Thread.sleep(500);

        logger.info("Enable auto-renewal...");
        contractInfo.clickAutoRenewalTumbler();
        Thread.sleep(500);

        logger.info("Fill the fields back...");
        contractInfo.setSubsequentTermMonths("1");
        renewalVal = contractInfo.getRenewal(); // since Renewal sets automatically => save it
        contractInfo.setSubsequentTermNotification("3 days");
        contractInfo.setRenewalEmailTo("test@parleypro.com");
        contractInfo.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract has been updated."));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void validateSavedFieldsRefreshAndValidateAgain()
    {
        logger.info("Assert that fields were saved...");

        Assert.assertEquals(contractInfo.getSubsequentTermMonths(), "1", "Subsequent term (months) value wasn't saved !!!");
        Assert.assertEquals(contractInfo.getRenewal(), renewalVal, "Renewal value wasn't saved !!!");
        Assert.assertEquals(contractInfo.getSubsequentTermNotification(), "3d", "Subsequent term Notification value wasn't saved !!!");

        Screenshoter.makeScreenshot();

        logger.info("Refresh page...");
        Selenide.refresh();

        contractInfo = new ContractInfo();

        logger.info("Assert again after refreshing...");
        Assert.assertEquals(contractInfo.getSubsequentTermMonths(), "1", "Subsequent term (months) value wasn't saved !!!");
        Assert.assertEquals(contractInfo.getRenewal(), renewalVal, "Renewal value wasn't saved !!!");
        Assert.assertEquals(contractInfo.getSubsequentTermNotification(), "3d", "Subsequent term Notification value wasn't saved !!!");
    }
}

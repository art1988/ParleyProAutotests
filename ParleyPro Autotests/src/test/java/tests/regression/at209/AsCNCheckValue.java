package tests.regression.at209;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AsCNCheckValue
{
    private static Logger logger = Logger.getLogger(AsCNCheckValue.class);

    @Test
    public void asCNCheckValue()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        loginPage.clickSignIn().getSideBar().clickInProgressContracts(false).selectContract(Cache.getInstance().getCachedContractTitle());

        logger.info("Checking contract value from header...");
        $(".contract-header__company-price").shouldBe(Condition.visible).shouldHave(Condition.exactText("USD 177,753,082.00"));

        new OpenedContract().clickContractInfo();
        Assert.assertEquals($("#contractValue").getValue(), "177,753,082.00");
        Screenshoter.makeScreenshot();
    }
}

package tests.regression.at209;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Cache;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class AsCNCheckValue
{
    private static Logger logger = Logger.getLogger(AsCNCheckValue.class);

    @Test
    @Description("This test checks that value is still visible on contract header and on Contract Info panel.")
    public void asCNCheckValue()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        loginPage.clickSignIn().getSideBar().clickInProgressContracts(false).selectContract(Cache.getInstance().getCachedContractTitle());

        logger.info("Checking contract value from header...");
        $(".contract-header__company-price").shouldBe(Condition.visible).shouldHave(Condition.exactText("USD 177,753,082.00"));

        logger.info("Checking contract value from Contract Info panel...");
        new OpenedContract().clickContractInfo();
        Assert.assertEquals($("#contractValue").getValue(), "177,753,082.00");
        Screenshoter.makeScreenshot();
    }
}

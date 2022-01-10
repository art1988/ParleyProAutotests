package tests.requests.at210;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class ConvertRequestToContract
{
    private static Logger logger = Logger.getLogger(ConvertRequestToContract.class);


    @Test(priority = 1)
    @Description("Final check happens here. This test converts request to contract and checks that ‘Request' label disappears and 'Add Documents’ section is shown.")
    public void convertRequestToContract()
    {
        logger.info("Login as user with full rights (Felix)...");

        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.USER_FELIX.getEmail());
        loginPage.setPassword(Const.USER_FELIX.getPassword());

        loginPage.clickSignIn().getSideBar().clickInProgressContracts(false).selectContract("Request for at-210");

        ContractInfo contractInfo = new ContractInfo(true);
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        $(".label.label_theme_purple").should(Condition.disappear);
        $(".documents-add__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Add Documents"));
        $(".js-upload-my-team-document-btn").shouldBe(Condition.visible);
        $(".js-upload-cp-document-btn").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsFelix()
    {
        new DashboardPage().getSideBar().logout();
    }
}

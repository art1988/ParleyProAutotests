package tests.requests.at175;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddRandomContract
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(AddRandomContract.class);

    @Test(priority = 1)
    public void addRandomContract()
    {
        logger.info("Login as CN...");

        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        dashboardPage = loginPage.clickSignIn();

        logger.info("Making random contract with name 'Test AT-175'...");
        ContractInformation contractInformation = dashboardPage.getSideBar().clickInProgressContracts(false).clickNewContractButton();

        contractInformation.setContractTitle("Test AT-175");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        $(".contract-header__name").shouldHave(Condition.text("Test AT-175"));
    }

    @Test(priority = 2)
    public void logoutAsCN()
    {
        dashboardPage.getSideBar().logout();
    }
}

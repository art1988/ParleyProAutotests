package tests.requests.at215;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AsCNConvertRequestToContract
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(AsCNConvertRequestToContract.class);


    @Test(priority = 1)
    public void loginAsCNAndConvertRequestToContract()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-215 Request");

        ContractInfo contractInfo = new ContractInfo(true);

        contractInfo.setCounterpartyOrganization("CounterpartyAT");
        contractInfo.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("As CN make sure that in-progress page has just converted contract...");
        sideBar.clickInProgressContracts(false);
        $(byText("AT-215 Request")).shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsCN()
    {
        logger.info("Logout as CN...");
        sideBar.logout();
    }
}

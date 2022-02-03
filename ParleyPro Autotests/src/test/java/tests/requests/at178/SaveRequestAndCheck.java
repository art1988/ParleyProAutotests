package tests.requests.at178;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class SaveRequestAndCheck
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(SaveRequestAndCheck.class);

    @Test(priority = 1)
    public void loginAsCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        dashboardPage = loginPage.clickSignIn();
    }

    @Test(priority = 2)
    public void saveRequestAndCheck() throws InterruptedException
    {
        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Contract request");

        ContractInfo contractInfo = new ContractInfo(true);

        contractInfo.setContractValue("2500");
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

        logger.info("Checking that user " + Const.USER_MARY.getFirstName() + " " + Const.USER_MARY.getLastName() + " wasn't triggered...");
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "RL", "CC"));
        $$(".document__score .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "CC"));

        Screenshoter.makeScreenshot();

        logger.info("Adding some text to trigger user...");
        new OpenedContract().clickByParagraph("Paragraph 2")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " Added more text"})
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));

        logger.info("Checking that user " + Const.USER_MARY.getFirstName() + " " + Const.USER_MARY.getLastName() + " was triggered...");
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "MJ", "RL", "CC"));
        $$(".document__score .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "MJ", "CC"));

        Screenshoter.makeScreenshot();
    }
}

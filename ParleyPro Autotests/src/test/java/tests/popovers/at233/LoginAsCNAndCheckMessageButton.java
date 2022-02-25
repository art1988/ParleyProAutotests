package tests.popovers.at233;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.InProgressContractsPage;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCNAndCheckMessageButton
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsCNAndCheckMessageButton.class);


    @Test
    public void loginAsCNAndCheckMessageButton()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        checkMessageButtonOfRequest();
        convertRequestToContract();
        checkMessageButtonOfContractAfterConversion();

        sideBar.logout();
    }

    @Step("Visit just created request and check presence of Message button for requester")
    public void checkMessageButtonOfRequest()
    {
        InProgressContractsPage inProgressContractsPage = sideBar.clickInProgressContracts(false);
        $(withText("Request_for_AT223")).shouldBe(Condition.visible);

        logger.info("As CN open request...");
        inProgressContractsPage.selectContract("Request_for_AT223");

        logger.info("Hover over Requester’s avatar and check Message button...");
        $$(".contract-header-users .user").filterBy(Condition.exactText("RL")).first().hover();
        $(".rc-tooltip-content").should(Condition.appear);
        $(".rc-tooltip-inner .spinner").should(Condition.disappear);
        $(".rc-tooltip-content button").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.text("MESSAGE"));
        Screenshoter.makeScreenshot();
    }

    @Step("Convert request to contract")
    public void convertRequestToContract()
    {
        ContractInfo contractInfo = new ContractInfo(true);

        logger.info("Filling all Contract request fields...");
        contractInfo.setCounterpartyOrganization("CounterpartyAT");
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        logger.info("Making sure that it is no longer a request...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
        $(".contract-header__right .label").should(Condition.disappear); // no REQUEST label
    }

    @Step("Check presence of Message button of contract after conversion")
    public void checkMessageButtonOfContractAfterConversion()
    {
        logger.info("Checking presence of Message button of contract after conversion...");
        $$(".contract-header-users .user").filterBy(Condition.exactText("RL")).first().hover();
        $(".rc-tooltip-content").should(Condition.appear);
        $(".rc-tooltip-inner .spinner").should(Condition.disappear);
        $(".rc-tooltip-content button").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.text("MESSAGE"));
        Screenshoter.makeScreenshot();
    }
}

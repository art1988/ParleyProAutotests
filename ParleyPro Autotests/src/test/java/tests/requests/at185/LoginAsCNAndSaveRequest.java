package tests.requests.at185;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCNAndSaveRequest
{
    private static Logger logger = Logger.getLogger(LoginAsCNAndSaveRequest.class);

    @Test
    public void loginAsCNAndSaveRequest()
    {
        // Login as CN
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Request for at-185");

        logger.info("Making sure that [Counterparty Organization] and [CCN] are filled in in the Contract Info...");

        ContractInfo contractInfo = new ContractInfo(true);

        Assert.assertEquals($("#counterpartyorganization").getValue(), "CounterpartyAT", "Counterparty organization has wrong value !!!");
        Assert.assertEquals($("#counterpartyOrganization").closest(".Select").find(".Select-value").getText() , "CounterpartyAT", "Counterparty organization has wrong value !!!");

        Assert.assertEquals($("#counterpartychiefnegotiator").getValue(), "arthur.khasanov+cpat@parleypro.com", "Counterparty Chief Negotiator has wrong value !!!");
        Assert.assertTrue($("#counterpartyChiefNegotiator").getValue().contains("arthur.khasanov+cpat@parleypro.com"), "Counterparty Chief Negotiator has wrong value !!!");

        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        logger.info("Making sure that Request successfully converted to contract...");
        $(".label.label_theme_purple").should(Condition.disappear); // no 'Request' label
        $(".contract-header__right button").shouldBe(Condition.visible).shouldHave(Condition.text("SEND INVITE")); // Send Invite button appears

        $$(".document__header.unselectable .lifecycle__item").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("DRAFT", "NEGOTIATE"));
        $$(".document__header.unselectable .lifecycle__item").findBy(Condition.exactText("DRAFT")).closest(".document__header").find(".document__header-rename > span")
                .shouldHave(Condition.exactText("AT185-Lists-D2")); // my team doc is in draft
        $$(".document__header.unselectable .lifecycle__item").findBy(Condition.exactText("NEGOTIATE")).closest(".document__header").find(".document__header-rename > span")
                .shouldHave(Condition.exactText("AT185-Manufacturing Agreement-redlines-D1")); // doc with redlines is in negotiate
        $$(".document__header.unselectable .lifecycle__item").findBy(Condition.exactText("NEGOTIATE")).closest(".document__header").find(".discussion-indicator__count")
                .shouldHave(Condition.exactText("2")); // doc that is in NEGOTIATE has 2 discussions

        Screenshoter.makeScreenshot();

        $(".menu-disc-doc__discussions").click(); // click by DISCUSSIONS tab
        logger.info("Assert that 2 discussions are external and have 'Need response' badge...");
        $$(".discussion2-label__status").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("EXTERNAL", "EXTERNAL"));
        $$("div[class^='style__actions']").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEED RESPONSE", "NEED RESPONSE"));

        Screenshoter.makeScreenshot();
    }
}

package tests.requests.at219;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCNAndCancel
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsCNAndCancel.class);


    @Test(priority = 1)
    public void loginAsCNAndConvertRequestToContract()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
        logger.info("Convert request to contract...");
        sideBar.clickInProgressContracts(false).selectContract("request for at-219");

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

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }

    @Test(priority = 2)
    public void deleteUserWithRequesterRole()
    {
        logger.info("Delete USER_AT219_Requester...");
        sideBar.clickAdministration().clickManageUsersTab().clickActionMenu("USER_AT219_Requester")
                .clickDelete(new User("USER_AT219_RequesterUSER_AT219_Requester fn", "USER_AT219_Requester ln", "arthur.khasanov+at219_requester@parleypro.com", "Parley650!"))
                .clickDelete();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" USER_AT219_Requester ln deleted successfully"));
    }

    @Test(priority = 3)
    @Description("Final assertion happens here: test verifies that contract can be canceled.")
    public void cancelContract()
    {
        sideBar.clickInProgressContracts(false).selectContract("request for at-219");

        new OpenedContract().clickContractActionsMenu().clickCancelContract().clickCancelContract();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract request for at-219 has been cancelled."));

        logger.info("Assert that CANCELLED label was shown...");
        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("CANCELLED"));
        Screenshoter.makeScreenshot();
    }
}

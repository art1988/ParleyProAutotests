package tests.requests.at219;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AsCNDeleteAndCancel
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(AsCNDeleteAndCancel.class);


    @Test(priority = 1)
    public void asCNConvertRequestsToContract()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
        logger.info("Convert requests to contract...");

        // 1
        sideBar.clickInProgressContracts(false).selectContract("request for at-219 DELETE_ME");

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

        // 2
        sideBar.clickInProgressContracts(false).selectContract("request for at-219 CANCEL_ME");

        contractInfo = new ContractInfo(true);
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
    @Description("Test requires to delete user with requester role.")
    public void deleteUserWithRequesterRole()
    {
        logger.info("Delete USER_AT219_Requester...");
        sideBar.clickAdministration().clickManageUsersTab().clickActionMenu("USER_AT219_Requester")
               .clickDelete(new User("USER_AT219_Requester fn", "USER_AT219_Requester ln", "arthur.khasanov+at219_requester@parleypro.com", "Parley650!"))
               .clickDelete();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" USER_AT219_Requester ln deleted successfully"));
    }

    @Test(priority = 3)
    @Description("Final assertions happens here: test verifies that contract can be deleted and cancelled.")
    public void deleteContractCancelContract()
    {
        logger.info("Trying to delete contract...");
        sideBar.clickInProgressContracts(false).selectContract("request for at-219 DELETE_ME");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract request for at-219 DELETE_ME has been deleted."));
        $(byText("request for at-219 DELETE_ME")).shouldNotBe(Condition.visible);


        logger.info("Trying to cancel contract...");
        sideBar.clickInProgressContracts(false).selectContract("request for at-219 CANCEL_ME");
        new OpenedContract().clickContractActionsMenu().clickCancelContract().clickCancelContract();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract request for at-219 CANCEL_ME has been cancelled."));
        logger.info("Assert that CANCELLED label was shown...");
        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("CANCELLED"));

        logger.info("Visit in-progress again and check that there is no cancelled contract in the list...");
        sideBar.clickInProgressContracts(true);
        $(byText("request for at-219 CANCEL_ME")).shouldNotBe(Condition.visible);
    }
}

package tests.requests.at215;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class CreateRequestAndReassignCN
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(CreateRequestAndReassignCN.class);


    @Test(priority = 1)
    public void loginAsFelixAddRequestAndReassignCN() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        logger.info("Login as user with full rights(Felix)...");
        loginPage.setEmail( Const.USER_FELIX.getEmail() );
        loginPage.setPassword( Const.USER_FELIX.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        ContractRequest contractRequest = sideBar.clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("AT-215 Request");
        contractRequest.setValueForSelect("ReqField_AT215_Trigger", "v1");
        contractRequest.uploadCounterpartyDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(byText("AT-215 Request")).shouldBe(Condition.visible);
        $(".label_theme_purple").shouldBe(Condition.visible);
        $(".contract-item .contracts-list__chief-negotiator-cell").shouldHave(Condition.text(Const.USER_FELIX.getFirstName() + " " + Const.USER_FELIX.getLastName()));

        sideBar.clickInProgressContracts(false).selectContract("AT-215 Request");
        new OpenedContract().clickContractActionsMenu()
                            .clickReassignChiefNegotiator()
                            .setChiefNegotiator(Const.PREDEFINED_USER_CN_ROLE.getEmail())
                            .clickReassign();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("reassigned to"));

        $(byText("AT-215 Request")).shouldBe(Condition.visible);
        $(".label_theme_purple").shouldBe(Condition.visible);
        $(".contract-item .contracts-list__chief-negotiator-cell").shouldHave(Condition.text(Const.PREDEFINED_USER_CN_ROLE.getFirstName() + " " + Const.PREDEFINED_USER_CN_ROLE.getLastName()));
    }

    @Test(priority = 2)
    public void logoutAsFelix()
    {
        logger.info("Logout as user with full rights(Felix)...");
        sideBar.logout();
    }
}

package tests.requests.at210;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OpenedContract;
import pages.administration.Fields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private SideBar sideBar;

    @Test(priority = 1)
    public void removeContract()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
        sideBar.clickInProgressContracts(false).selectContract("Request for at-210");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();

        $(byText("Request for at-210")).shouldNotBe(Condition.visible);
    }

    @Test(priority = 2)
    public void removeWorkflow()
    {
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("AT-210 routing_workflow").clickDelete().clickDelete();

        $(byText("AT-210 routing_workflow")).shouldNotBe(Condition.visible);
    }

    @Test(priority = 3)
    public void removeField()
    {
        Fields fieldsTab = sideBar.clickAdministration().clickFieldsTab();

        fieldsTab.clickContractFields().removeField("ReqField_AT210", "Contract Request").clickDelete();
        fieldsTab.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));

        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Contract Request\")').parent().find('.js-item').length === 0"),
                "Looks like that not all fields were removed from Contract Request !!!");
    }
}

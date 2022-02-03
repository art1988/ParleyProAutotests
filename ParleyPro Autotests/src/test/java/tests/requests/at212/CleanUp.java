package tests.requests.at212;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.administration.Fields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private SideBar sideBar;


    @Test(priority = 1)
    public void loginAsCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
    }

    @Test(priority = 2)
    public void revertRoleForRequester()
    {
        sideBar.clickAdministration().clickManageUsersTab().clickActionMenu("Requester_User ln").clickEdit().deleteRole("Contract Manager").clickUpdateUser();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("User Requester_User ln updated successfully"));
    }

    @Test(priority = 3)
    public void removeWorkflow()
    {
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("AT-212_Routing_Workflow").clickDelete().clickDelete();
        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));
    }

    @Test(priority = 4)
    public void removeRequestFields()
    {
        Fields fieldsTab = sideBar.clickAdministration().clickFieldsTab();

        fieldsTab.clickContractFields().removeField("ReqField_AT212_Trigger", "Contract Request").clickDelete();
        fieldsTab.clickContractFields().removeField("ReqField_AT212_SELECT", "Contract Request").clickDelete();
        fieldsTab.clickContractFields().removeField("ReqField_AT212_TEXT", "Contract Request").clickDelete();
        fieldsTab.clickContractFields().removeField("ReqField_AT212_NUM", "Contract Request").clickDelete();
        fieldsTab.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));

        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Contract Request\")').parent().find('.js-item').length === 0"),
                "Looks like that not all fields were removed from Contract Request !!!");
    }
}

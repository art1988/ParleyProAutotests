package tests.popovers.at233;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Step;
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
public class RevertStateBack
{
    private SideBar sideBar;

    @Test
    public void revertStateBack()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        deleteContract();
        removeViewerPlusRole();
        deleteWorkflow();
        deleteRequestField();
    }

    @Step("Delete contract by the name [Request_for_AT223]")
    public void deleteContract()
    {
        sideBar.clickInProgressContracts(false).selectContract("Request_for_AT223");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract Request_for_AT223 has been deleted."));
    }

    @Step("Remove [Viewer Plus] role from user Requester_User ln")
    public void removeViewerPlusRole()
    {
        sideBar.clickAdministration()
                .clickManageUsersTab()
                .clickActionMenu("Requester_User ln")
                .clickEdit()
                .deleteRole("Viewer Plus")
                .clickUpdateUser();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("User Requester_User ln updated successfully"));
    }

    @Step("Delete [Routing_Workflow_for_AT-233] workflow")
    public void deleteWorkflow()
    {
        sideBar.clickAdministration()
               .clickWorkflowsTab()
               .clickActionMenu("Routing_Workflow_for_AT-233")
               .clickDelete().clickDelete();

        $(byText("Routing_Workflow_for_AT-233")).shouldNotBe(Condition.visible);
    }

    @Step("Delete [AT-233_trigger_field] request field")
    public void deleteRequestField()
    {
        Fields fieldsPage = sideBar.clickAdministration().clickFieldsTab();

        fieldsPage.clickContractFields().removeField("AT-233_trigger_field").clickDelete();
        fieldsPage.clickSave();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract fields have been saved."));
    }
}

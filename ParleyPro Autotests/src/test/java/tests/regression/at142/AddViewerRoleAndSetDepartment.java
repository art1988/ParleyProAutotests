package tests.regression.at142;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.ManageUsers;

import static com.codeborne.selenide.Selenide.$;


public class AddViewerRoleAndSetDepartment
{
    @Test
    @Description("Adds Viewer role to user Greg and sets to view only Dep1.")
    public void addViewerRoleAndSetDepartment()
    {
        ManageUsers manageUsersTab = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        manageUsersTab.clickActionMenu( Const.USER_GREG.getFirstName() )
                      .clickEdit()
                      .clickAddRole()
                      .setRole("Viewer")
                      .setDepartment("Dep1")
                      .clickUpdateUser();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("updated successfully"));

        // Logout
        new DashboardPage().getSideBar().logout();
    }
}

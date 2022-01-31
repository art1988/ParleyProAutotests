package tests.requests.at219;

import com.codeborne.selenide.Condition;
import forms.add.AddNewUser;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddUserAgain
{
    @Test(priority = 1)
    public void addUserWithRequesterRole()
    {
        AddNewUser addNewUser = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab().clickAddNewUser();

        addNewUser.setFirstName("USER_AT219_Requester");
        addNewUser.setEmail("arthur.khasanov+at219_requester@parleypro.com");
        addNewUser.clickAddRole().setRole("Requester");
        addNewUser.clickAddUser();

        $(byText("USER_AT219_Requester")).shouldBe(Condition.visible);
    }
}

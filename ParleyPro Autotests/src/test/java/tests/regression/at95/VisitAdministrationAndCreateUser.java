package tests.regression.at95;

import forms.add.AddNewUser;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.ManageUsers;
import utils.Screenshoter;

public class VisitAdministrationAndCreateUser
{
    private static Logger logger = Logger.getLogger(VisitAdministrationAndCreateUser.class);

    @Test(priority = 1)
    public void visitAdministrationAndCreateUser()
    {
        ManageUsers manageUsersPage = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        AddNewUser addNewUserForm = manageUsersPage.clickAddNewUser();

        addNewUserForm.setEmail("junk@junk.com");
        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Viewer Plus");
        addNewUserForm.setRegion("region2");
        addNewUserForm.setContractCategory("category1");

        logger.info("Assert that 'Region' value is region2...");
        Assert.assertEquals(addNewUserForm.getRegion(), "region2", "Looks like that 'Region' value is wrong !");

        logger.info("Assert that 'Contract category' is category1...");
        Assert.assertEquals(addNewUserForm.getContractCategory(), "category1", "Looks like that 'Contract category' value is wrong !");

        Screenshoter.makeScreenshot();
    }
}

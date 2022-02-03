package tests.regression.at95;

import forms.add.AddNewUser;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.ManageUsers;
import utils.Screenshoter;


public class VisitAdministrationCreateUserAndChangeValues
{
    private static Logger logger = Logger.getLogger(VisitAdministrationCreateUserAndChangeValues.class);

    @Test(priority = 1)
    @Description("This test clicks add new user in Administration, changes Region/Contract category/Department values and asserts that previous values are still the same.")
    public void visitAdministrationAndCreateUser()
    {
        ManageUsers manageUsersTab = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        AddNewUser addNewUserForm = manageUsersTab.clickAddNewUser();

        addNewUserForm.setEmail("junk@junk.com");
        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Viewer Plus");
        addNewUserForm.setRegion("region2");
        addNewUserForm.setContractCategory("category1");

        logger.info("Assert that 'Region' value is still region2...");
        Assert.assertEquals(addNewUserForm.getRegion(), "region2", "Looks like that 'Region' value is wrong !");

        logger.info("Assert that 'Contract category' is still category1...");
        Assert.assertEquals(addNewUserForm.getContractCategory(), "category1", "Looks like that 'Contract category' value is wrong !");

        addNewUserForm.setDepartment("department2");

        logger.info("Assert that other values are still the same...");
        Assert.assertEquals(addNewUserForm.getRegion(), "region2", "Looks like that 'Region' value is wrong !");
        Assert.assertEquals(addNewUserForm.getContractCategory(), "category1", "Looks like that 'Contract category' value is wrong !");

        addNewUserForm.setRegion("region1");

        logger.info("Assert that other values are still the same...");
        Assert.assertEquals(addNewUserForm.getContractCategory(), "category1", "Looks like that 'Contract category' value is wrong !");
        Assert.assertEquals(addNewUserForm.getDepartment(), "department2", "Looks like that 'Department' value is wrong !");
        Assert.assertEquals(addNewUserForm.getRegion(), "region1", "Looks like that 'Region' value is wrong !");

        addNewUserForm.setContractCategory("category2");

        logger.info("Assert that other values are still the same...");
        Assert.assertEquals(addNewUserForm.getRegion(), "region1", "Looks like that 'Region' value is wrong !");
        Assert.assertEquals(addNewUserForm.getContractCategory(), "category2", "Looks like that 'Contract category' value is wrong !");
        Assert.assertEquals(addNewUserForm.getDepartment(), "department2", "Looks like that 'Department' value is wrong !");

        Screenshoter.makeScreenshot();

        addNewUserForm.clickCancel();
    }
}

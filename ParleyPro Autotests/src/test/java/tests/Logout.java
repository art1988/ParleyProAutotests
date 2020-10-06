package tests;

import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

public class Logout
{
    @Test
    public void logout()
    {
        DashboardPage dashboardPage = new DashboardPage();

        LoginPage loginPage = dashboardPage.getSideBar().logout();
    }
}

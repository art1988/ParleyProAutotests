package tests.fields.at115;

import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.fields_breadcrumb.Layout;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsMyTeamCNAndCheckLayoutPage
{
    private static Logger logger = Logger.getLogger(LoginAsMyTeamCNAndCheckLayoutPage.class);

    @Test
    @Description("This simple test checks that page Administration > Fields > Layout opens without gray screen.")
    public void loginAsMyTeamCNAndCheckLayoutPage()
    {
        Layout layoutPage = new DashboardPage().getSideBar()
                                               .clickAdministration()
                                               .clickFieldsTab()
                                               .clickLayout();

        logger.info("Making sure that no gray screen happened...");
        Assert.assertEquals($$(".admin-fields-section .row .admin-fields-layout-field").size(), 6,
                "The amount of fields on LAYOUT page is wrong !!!");

        Screenshoter.makeScreenshot();
    }
}

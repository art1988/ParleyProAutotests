package tests.fields.at177;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import tests.LoginBase;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class DeleteTemplateFromT1
{
    private static Logger logger = Logger.getLogger(DeleteTemplateFromT1.class);

    @Test
    @Description("Deletes template from tenant T1.")
    public void deleteTemplateFromT1()
    {
        LoginBase loginBase = new LoginBase();

        if( LoginBase.isProd() )
        {
            Selenide.open(loginBase.getTenantUrl());

            LoginPage loginPage = new LoginPage();

            loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
            loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

            DashboardPage dashboardPage = loginPage.clickSignIn();

            dashboardPage.getSideBar().clickTemplates(false).clickActionMenuTemplate("Template_AT-77_dummy").clickDelete().clickDelete();

            $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));
        }
        else
        {
            // already logged in => just delete template
            new DashboardPage().getSideBar().clickTemplates(false).clickActionMenuTemplate("Template_AT-77_dummy").clickDelete().clickDelete();

            $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));
        }
    }
}

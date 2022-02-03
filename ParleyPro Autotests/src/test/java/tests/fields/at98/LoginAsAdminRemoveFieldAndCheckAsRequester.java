package tests.fields.at98;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.administration.Fields;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsAdminRemoveFieldAndCheckAsRequester
{
    private static Logger logger = Logger.getLogger(LoginAsAdminRemoveFieldAndCheckAsRequester.class);

    @Test(priority = 1)
    public void loginAsAdminAndRemoveField()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn();

        Fields fieldsPage = dashboardPage.getSideBar().clickAdministration().clickFieldsTab();
        fieldsPage.clickContractFields().removeField("f2").clickDelete();

        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        dashboardPage.getSideBar().logout();
    }

    @Test(priority = 2)
    @Description("This test logins as requester and making sure that only f1 is present on the form.")
    public void loginAsRequesterAndCheckField() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequestForm = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();
        contractRequestForm.selectValueForField("R1", "val1");
        Thread.sleep(1_000);

        logger.info("Making sure that only f1 is present on the form...");
        $(".spinner").waitUntil(Condition.disappear, 10_000);
        $("label[for='f1']").waitUntil(Condition.visible, 7_000);
        $("label[for='f2']").shouldBe(Condition.hidden);

        Assert.assertTrue(Selenide.executeJavaScript("return $('.row label:contains(\"f1\")').length === 1"), "Looks like that f1 label is not on the form !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.row label:contains(\"f1\")').parent().find(\"input\").length === 1"), "Looks like that f1 doesn't have field !!!");

        Assert.assertFalse(Selenide.executeJavaScript("return $('.row label:contains(\"f2\")').length === 1"), "f2 label is also on form, but shouldn't !!!");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.row label:contains(\"f2\")').parent().find(\"input\").length === 1"), "f2 field is also on form, but shouldn't !!!");

        Screenshoter.makeScreenshot();
        contractRequestForm.clickCancel();
        dashboardPage.getSideBar().logout();
    }
}

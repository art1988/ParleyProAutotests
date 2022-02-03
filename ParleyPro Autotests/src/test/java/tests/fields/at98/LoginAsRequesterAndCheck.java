package tests.fields.at98;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class LoginAsRequesterAndCheck
{
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndCheck.class);

    @Test
    @Description("This test logins as requester and checks that fields f1 and f2 are present on page after selecting of val1.")
    public void loginAsRequesterAndCheck()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequestForm = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();
        contractRequestForm.selectValueForField("R1", "val1");

        logger.info("Making sure that f1 and f2 are present on the form...");
        $(".spinner").waitUntil(Condition.disappear, 10_000);
        $("label[for='f1']").waitUntil(Condition.visible, 7_000);
        $("label[for='f2']").waitUntil(Condition.visible, 7_000);

        Assert.assertTrue(Selenide.executeJavaScript("return $('.row label:contains(\"f1\")').length === 1"), "Looks like that f1 label is not on the form !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.row label:contains(\"f1\")').parent().find(\"input\").length === 1"), "Looks like that f1 doesn't have field !!!");

        Assert.assertTrue(Selenide.executeJavaScript("return $('.row label:contains(\"f2\")').length === 1"), "Looks like that f2 label is not on the form !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.row label:contains(\"f2\")').parent().find(\"input\").length === 1"), "Looks like that f2 doesn't have field !!!");

        Screenshoter.makeScreenshot();
        contractRequestForm.clickCancel();
        dashboardPage.getSideBar().logout();
    }
}

package tests.fields.at98;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;

import static com.codeborne.selenide.Selenide.$;


public class RemoveFields
{
    private static Logger logger = Logger.getLogger(RemoveFields.class);

    @Test
    public void removeFields()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn();

        Fields fieldsPage = dashboardPage.getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFieldsTab = fieldsPage.clickContractFields();
        contractFieldsTab.removeField("R1").clickDelete();
        contractFieldsTab.removeField("f1").clickDelete();

        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertFalse($("input[value='R1']").isDisplayed());
        Assert.assertFalse($("input[value='f1']").isDisplayed());
        Assert.assertFalse($("input[value='f2']").isDisplayed());
    }
}

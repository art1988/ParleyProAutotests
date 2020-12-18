package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * This class represents Select tenant page with multiple options of tenants to choose
 */
public class SelectTenantPage
{
    public SelectTenantPage()
    {
        boolean hasLogo = $(".auth__head").getCssValue("background").contains("images/cc8124f8be69a02e7221cfaabe5a0ef1.svg");

        Assert.assertTrue(hasLogo);

        $(".auth__title").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("Select tenant"));
        $(".button.btn-common.btn.btn-link").waitUntil(Condition.visible, 5_000).should(Condition.exactText("BACK TO SIGN IN"));
        $(".auth-sign-in-tenants__table").should(Condition.visible);
    }

    public DashboardPage selectTenant(String tenantName)
    {
        // Exact match by tenantName
        Selenide.executeJavaScript("$('.auth-sign-in-tenants__table .auth-sign-in-tenants__cell').filter(function() { return $(this).text() === '" + tenantName + "'; }).click()");

        return new DashboardPage();
    }
}

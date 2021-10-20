package tests.fields.at177;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;

import static com.codeborne.selenide.Selenide.$;

public class DeleteTemplateFromT1
{
    @Test
    @Description("Deletes template from tenant T1.")
    public void deleteTemplateFromT1()
    {
        // already logged in => just delete template
        new DashboardPage().getSideBar().clickTemplates(false).clickActionMenuTemplate("Template_AT-77_dummy").clickDelete().clickDelete();

        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));

        new DashboardPage().getSideBar().logout();
    }
}

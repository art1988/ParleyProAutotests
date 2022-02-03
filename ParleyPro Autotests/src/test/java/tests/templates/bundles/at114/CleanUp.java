package tests.templates.bundles.at114;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;

import static com.codeborne.selenide.Selenide.$;


public class CleanUp
{
    @Test
    public void cleanUp()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(false);

        templatesPage.clickActionMenuTemplate("Template_AT-86_text_cut_off").clickDelete().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);

        templatesPage.clickActionMenuTemplate("Template_AT-77_dummy").clickDelete().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);


        templatesPage.clickBundlesTab();
        templatesPage.clickActionMenuBundle("TEST Bundle AKM").clickDelete().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Bundle TEST Bundle AKM has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);
    }
}

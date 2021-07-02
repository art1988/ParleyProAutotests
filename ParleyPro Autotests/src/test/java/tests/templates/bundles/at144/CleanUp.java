package tests.templates.bundles.at144;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    @Test
    public void cleanUp()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(false).clickTemplatesTab();

        String[] templatesToDelete = {"image", "dummyAT141", "AT-135_Template_identical"};

        for( int i = 0; i < templatesToDelete.length; i++ )
        {
            templatesPage.clickActionMenuTemplate( templatesToDelete[i] ).clickDelete().clickDelete();

            $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Template " + templatesToDelete[i] + " has been deleted."));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);
        }

        templatesPage = templatesPage.clickBundlesTab();


        String[] bundlesToDelete = {"qwer", "asdf", "qwas"};

        for( int i = 0; i < bundlesToDelete.length; i++ )
        {
            templatesPage.clickActionMenuBundle( bundlesToDelete[i] ).clickDelete().clickDelete();

            $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Bundle " + bundlesToDelete[i] + " has been deleted."));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);
        }

        $(".templates-board__empty").shouldBe(Condition.visible).shouldHave(Condition.exactText("There are no bundles"));
    }
}

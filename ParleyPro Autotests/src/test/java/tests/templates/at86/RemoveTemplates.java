package tests.templates.at86;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RemoveTemplates
{
    @Test(priority = 1)
    public void removeTemplates()
    {
        new DashboardPage().getSideBar().clickTemplates(false).clickActionMenuTemplate("Template_AT-86_text_cut_off").clickDelete().clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        new DashboardPage().getSideBar().clickTemplates(false).clickActionMenuTemplate("downloaded_Modified").clickDelete().clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }

    @Test(priority = 2)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}

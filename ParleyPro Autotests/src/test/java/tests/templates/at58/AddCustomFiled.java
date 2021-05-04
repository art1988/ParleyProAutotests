package tests.templates.at58;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.FieldType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.administration.Fields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddCustomFiled
{
    @Test
    public void addCustomFiled()
    {
        Fields fieldTab = new SideBar().clickAdministration().clickFieldsTab();

        fieldTab.clickContractFields().createNewFiled("Summary", "Effective Date", FieldType.DATE, false);
        fieldTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();

        Selenide.refresh();

        new WebDriverWait(WebDriverRunner.getWebDriver(), 15).until(webDriver ->
                Selenide.executeJavaScript("return document.readyState").equals("complete"));
    }
}

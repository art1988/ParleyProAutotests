package tests.templates.at58;

import constants.FieldType;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.administration.Fields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class AddCustomFiled
{
    @Test
    public void addCustomFiled() throws InterruptedException
    {
        Fields fieldTab = new SideBar().clickAdministration().clickFieldsTab();

        fieldTab.createNewFiledForSummary("Effective Date", FieldType.DATE, false);

        Thread.sleep(10_000);
    }
}

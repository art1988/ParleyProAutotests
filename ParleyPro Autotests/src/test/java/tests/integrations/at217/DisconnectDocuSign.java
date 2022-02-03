package tests.integrations.at217;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class DisconnectDocuSign
{
    @Test
    public void disconnectDocuSign()
    {
        new DashboardPage().getSideBar()
                           .clickAdministration().clickIntegrationsTab().clickDisconnectDocuSign();

        $(".integrations-docusign__status").shouldHave(Condition.exactText("CONNECT"));
    }
}

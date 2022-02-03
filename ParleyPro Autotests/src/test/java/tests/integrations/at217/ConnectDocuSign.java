package tests.integrations.at217;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class ConnectDocuSign
{
    @Test
    public void connectDocuSign()
    {
        new DashboardPage().getSideBar()
                           .clickAdministration().clickIntegrationsTab().clickConnectDocuSign()
                           .setEmail("arthur.khasanov+docusign@parleypro.com")
                           .setPassword("Parley650!")
                           .clickSignIn();

        $(".integrations-docusign__status").shouldHave(Condition.exactText("CONNECTED"));
    }
}

package tests.integrations.at217;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;
import pages.DashboardPage;

import static com.codeborne.selenide.Selenide.$;


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

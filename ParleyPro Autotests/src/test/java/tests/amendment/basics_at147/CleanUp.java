package tests.amendment.basics_at147;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;

import static com.codeborne.selenide.Selenide.$;


public class CleanUp
{
    @Test(priority = 1)
    public void deleteAllContractsOnInProgressPage()
    {
        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);

        String[] contractsToDelete = {"Executed Signed-A-A", "Executed Signed-B", "Executed Expired-A", "New val for Executed Managed-A"};

        for( int i = 0; i < contractsToDelete.length; i++ )
        {
            inProgressContractsPage.selectContract( contractsToDelete[i] );
            new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
            $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text(" has been deleted."));
            $(".notification-stack").waitUntil(Condition.disappear, 20_000);

            if( i == contractsToDelete.length - 1 ) break;
            inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);
        }
    }

    @Test(priority = 2)
    public void deleteAllContractsOnExecutedPage()
    {
        ExecutedContractsPage executedContractsPage = new DashboardPage().getSideBar().clickExecutedContracts(false);

        String[] contractsToDelete = {"Executed Signed-A", "Executed Expired", "Executed Managed", "Executed Signed"};

        for( int i = 0; i < contractsToDelete.length; i++ )
        {
            executedContractsPage.selectContract( contractsToDelete[i] );
            new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
            $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text(" has been deleted."));
            $(".notification-stack").waitUntil(Condition.disappear, 20_000);

            if( i == contractsToDelete.length - 1 ) break;
            executedContractsPage = new DashboardPage().getSideBar().clickExecutedContracts(false);
        }
    }
}

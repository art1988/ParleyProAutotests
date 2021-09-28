package tests.contract_info.at176;

import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class AddInProgressContract
{
    @Test
    public void addInProgressContract() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Ipsum aliquam ut consequuntur sint. Velit placeat et veniam sint veritatis esse quidem. Iusto consequatur est fuga at voluptas molestias distinctio. Magni saepe quia qui ipsam quis voluptas qui. Exercitationem sit dolor ullam dolorum enim eaque enim. Optio ratione corrupti eos aut ut ratione velit ea");
        contractInformation.setDueDate("Jan 1, 2040");
        contractInformation.setContractValue("100000000000");
        contractInformation.setContractRadioButton("Buy");
        contractInformation.setMyCompanyTemplate(true);
        

        Thread.sleep(10_000);
    }
}

package tests.attachment_runner;

import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContract
{
    @Test
    public void createContract()
    {
        ContractInformation contractInformation = new SideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AttachmentRunner Contract");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();
    }
}

package tests.templates.at58;

import constants.FieldType;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContract
{
    @Test
    public void createContract() throws InterruptedException
    {
        ContractInformation contractInformation = new SideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Nurix: getDateFromCustomField");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.setValueForCustomField("Effective Date", FieldType.DATE, "Jul 1, 2020");

        contractInformation.clickSave();

        Thread.sleep(10_000);
    }
}

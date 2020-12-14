package tests.templates;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class AddDocumentFromTemplate
{
    @Test
    public void addDocumentFromTemplate() throws InterruptedException
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("ContractThatDoesNotMatchTemplate_AT48");

        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickSelectTemplateTab();
        addDocuments.selectTemplate("Template_AT48[ EDITED ]");

        Thread.sleep(5_000);
    }
}

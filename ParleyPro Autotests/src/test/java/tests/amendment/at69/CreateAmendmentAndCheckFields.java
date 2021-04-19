package tests.amendment.at69;

import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateAmendmentAndCheckFields
{
    private static Logger logger = Logger.getLogger(CreateAmendmentAndCheckFields.class);

    @Test
    public void createAmendmentAndCheckFields() throws InterruptedException
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("at-69 Amendment");

        ContractInformation contractInformationAfterAmend = new OpenedContract().clickContractActionsMenu()
                                                                                .clickAmendContract();

        logger.info("Assert fields on Contract information after clicking of amend...");
        Assert.assertEquals(contractInformationAfterAmend.getContractTitle(), "at-69 Amendment-A", "Wrong title of contract after amendment !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.js-linked-contracts-title:visible').text()"), "Linked contracts: 1", "Wrong number of linked contracts after amendment !!!");
        Assert.assertEquals($(".js-linked-contracts-head").getText(), "Amendment to:\nat-69 Amendment", "Amendment to refers to wrong contract !!!");

        // Scroll to 'Contract category' field
        Selenide.executeJavaScript("$('.modal-content label span:contains(\"category\")')[0].scrollIntoView({})");

        contractInformationAfterAmend.setContractCategory("Amendment - Testcat");

        //logger.info("Assert that all fields ");

        //...
    }
}

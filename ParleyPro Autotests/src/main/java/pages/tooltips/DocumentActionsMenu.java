package pages.tooltips;

import com.codeborne.selenide.Selenide;
import forms.DocumentFormattingOption;
import org.testng.Assert;
import pages.DownloadForInternalOrCounterparty;
import utils.Waiter;

public class DocumentActionsMenu
{
    private String documentName;


    public DocumentActionsMenu(String documentName)
    {
        this.documentName = documentName;

        Waiter.smartWaitUntilVisible("$('.document__menu .dropdown-menu.dropdown-menu-right:visible')");

        String allMenuItems = Selenide.executeJavaScript("return $('.document__menu .dropdown-menu.dropdown-menu-right:visible li[role=\"presentation\"]').text()");
        Assert.assertTrue(allMenuItems.contains("DownloadCancel"));
    }

    public DocumentFormattingOption clickFormat()
    {
        Selenide.executeJavaScript("$('.document__menu .dropdown-menu.dropdown-menu-right:visible li:contains(\"Format\")').find(\"a\")[0].click()");

        return new DocumentFormattingOption(documentName);
    }

    /**
     * Click download menu item when document is in negotiate status and contract is classic
     */
    public DownloadForInternalOrCounterparty clickDownload()
    {
        Selenide.executeJavaScript("$('.document__menu .dropdown-menu.dropdown-menu-right:visible li:contains(\"Download\")').find(\"a\")[0].click()");

        return new DownloadForInternalOrCounterparty();
    }
}

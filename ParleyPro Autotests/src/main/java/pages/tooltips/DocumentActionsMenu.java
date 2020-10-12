package pages.tooltips;

import com.codeborne.selenide.Selenide;
import forms.DocumentFormattingOption;
import org.testng.Assert;
import utils.Waiter;

public class DocumentActionsMenu
{
    private String documentName;


    public DocumentActionsMenu(String documentName)
    {
        this.documentName = documentName;

        Waiter.smartWaitUntilVisible("$('.document__menu .dropdown-menu.dropdown-menu-right:visible')");

        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__menu .dropdown-menu.dropdown-menu-right:visible li').text()"),
                "FormatDownloadCancelDelete");
    }

    public DocumentFormattingOption clickFormat()
    {
        Selenide.executeJavaScript("$('.document__menu .dropdown-menu.dropdown-menu-right:visible li:contains(\"Format\")').find(\"a\")[0].click()");

        return new DocumentFormattingOption(documentName);
    }
}

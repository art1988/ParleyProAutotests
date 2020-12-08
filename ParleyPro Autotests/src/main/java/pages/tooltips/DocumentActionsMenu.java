package pages.tooltips;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.DocumentFormattingOption;
import forms.UploadDocument;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.DownloadForInternalOrCounterparty;
import utils.Waiter;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents context menu that appears after clicking by 3 dotted button for document.
 * Menu Items may vary from stage of the document.
 *
 * So, for example DRAFT have: Format, Download, Cancel, Delete
 * Classic contract in NEGOTIATE have: Upload new version, Format, Download, Cancel, Delete
 */
public class DocumentActionsMenu
{
    private String documentName;


    private static Logger logger = Logger.getLogger(DocumentActionsMenu.class);

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

        logger.info("Format menu item was clicked");

        return new DocumentFormattingOption(documentName);
    }

    public UploadDocument clickUploadDocument(String documentName)
    {
        Selenide.executeJavaScript("$('.document__menu .dropdown-menu.dropdown-menu-right:visible li:contains(\"Upload document\")').find(\"a\")[0].click()");

        logger.info("Upload document menu item was clicked");

        return new UploadDocument(documentName);
    }

    public void clickCancelFormatting()
    {
        Selenide.executeJavaScript("$('.document__menu .dropdown-menu.dropdown-menu-right:visible li:contains(\"Cancel formatting\")').find(\"a\")[0].click()");

        logger.info("Cancel formatting menu item was clicked");
    }

    /**
     * Click download menu item.
     * May return DownloadForInternalOrCounterparty in case if document is in negotiate status and contract is classic, null otherwise.
     */
    public DownloadForInternalOrCounterparty clickDownload(boolean isClassic) throws FileNotFoundException
    {
        WebElement downloadMenuItem_webElem = Selenide.executeJavaScript("return $('.document__menu .dropdown-menu.dropdown-menu-right:visible li:contains(\"Download\")').find(\"a\")[0]");
        SelenideElement downloadMenuItem = $(downloadMenuItem_webElem);

        logger.info("Download menu item was clicked");

        if( isClassic )
        {
            downloadMenuItem.click();

            return new DownloadForInternalOrCounterparty(); // if document is in negotiate status and contract is classic
        }
        else
        {
            downloadMenuItem.download();

            return null;
        }
    }
}

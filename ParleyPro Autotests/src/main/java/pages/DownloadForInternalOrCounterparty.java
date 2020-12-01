package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents popup that displays after clicking on Download menu item of classic contract.
 * Allows to download document of both 'Internal use' or 'Counterparty'.
 */
public class DownloadForInternalOrCounterparty
{
    private SelenideElement downloadButtonGreen  = $("._button.scheme_green.size_lg");
    private SelenideElement downloadButtonPurple = $("._button.scheme_violet.size_lg");


    private static Logger logger = Logger.getLogger(DownloadForInternalOrCounterparty.class);

    public DownloadForInternalOrCounterparty()
    {
        $(".download-document__cell-title").waitUntil(Condition.visible, 7_000);

        Assert.assertEquals(Selenide.executeJavaScript("return $('.download-document__cell-title').eq(0).text()"), "Download for  internal use");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.download-document__cell-title').eq(1).text()"), "Download for Counterparty");

        $$(".download-document__ico").shouldHave(CollectionCondition.size(2));
    }

    public File clickDownloadForMyTeam() throws FileNotFoundException
    {
        logger.info("DOWNLOAD for my team button was clicked");

        return downloadButtonGreen.download();
    }

    public File clickDownloadForCounterparty() throws FileNotFoundException
    {
        logger.info("DOWNLOAD for Counterparty button was clicked");

        return downloadButtonPurple.download();
    }
}

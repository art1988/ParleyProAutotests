package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;

public class FormatDocumentInWord
{
    private SelenideElement cancelButton   = $(".modal-content .js-cancel-button");
    private SelenideElement downloadButton = $(".modal-content .js-download-button");


    private static Logger logger = Logger.getLogger(FormatDocumentInWord.class);

    public FormatDocumentInWord(String documentName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("You have requested to format document in Word."));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("\"" + documentName + "\" will be downloaded for editing"));
    }

    public File clickDownload() throws FileNotFoundException
    {
        logger.info("Download button was clicked");

        return downloadButton.download();
    }
}

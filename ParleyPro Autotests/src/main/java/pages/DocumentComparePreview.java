package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after uploading of new version of document
 */
public class DocumentComparePreview
{
    private String contractName;
    private SelenideElement title = $(".modal-body-title");
    private SelenideElement uploadButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(DocumentComparePreview.class);

    public DocumentComparePreview(String documentName, String contractName)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("\"" + documentName + "\" document compare preview"));

        this.contractName = contractName;
    }

    public String getCounterAdded()
    {
        return $(".update-document__added").getText();
    }

    public String getCounterEdited()
    {
        return $(".update-document__edited").getText();
    }

    public String getCounterDeleted()
    {
        return $(".update-document__deleted").getText();
    }

    public DiscussionsOfSingleContract clickUpload()
    {
        uploadButton.click();

        logger.info("UPLOAD button was clicked");

        return new DiscussionsOfSingleContract(contractName);
    }
}

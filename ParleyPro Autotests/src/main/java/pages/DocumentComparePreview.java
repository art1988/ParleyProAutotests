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

    /**
     * Use this constructor for classic contract
     * @param documentName
     * @param contractName
     */
    public DocumentComparePreview(String documentName, String contractName)
    {
        $(".spinner").waitUntil(Condition.disappear, 60_000);

        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("\"" + documentName + "\" document compare preview"));

        this.contractName = contractName;
    }

    /**
     * Use this constructor for non-classic contract
     * @param documentName
     */
    public DocumentComparePreview(String documentName)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("\"" + documentName + "\" document compare preview"));
    }

    public String getCounterAdded()
    {
        return $(".update-document__added").getText();
    }

    public String getCounterEdited()
    {
        return $(".update-document__edited").getText();
    }

    public String getCounterCommented()
    {
        return $(".update-document__commented").getText();
    }

    public String getCounterDeleted()
    {
        return $(".update-document__deleted").getText();
    }

    /**
     * Click Upload button. If contract is in classic mode then it will show DISCUSSIONS tab, otherwise - opened contract
     * @param isClassic
     * @return DiscussionsOfSingleContract if contract is in classic mode, null otherwise
     */
    public DiscussionsOfSingleContract clickUpload(boolean isClassic)
    {
        uploadButton.click();

        logger.info("UPLOAD button was clicked");

        if( isClassic )
        {
            return new DiscussionsOfSingleContract(contractName);
        }

        return null;
    }
}

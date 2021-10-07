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
    private SelenideElement uploadButton = $(".modal-body .button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(DocumentComparePreview.class);

    /**
     * Use this constructor for classic contract
     * @param documentName
     * @param contractName
     */
    public DocumentComparePreview(String documentName, String contractName)
    {
        try { Thread.sleep(2_000); } catch (InterruptedException e) { e.printStackTrace(); }

        logger.info("Waiting until spinner will disappear [up to 2 minutes]...");
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);

        title.waitUntil(Condition.visible, 60_000 * 2).shouldHave(Condition.exactText("\"" + documentName + "\" document compare preview"));

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
        SelenideElement counterAdded = $(".update-document__added");

        return counterAdded.is(Condition.exist) ? counterAdded.getText() : "";
    }

    public String getCounterEdited()
    {
        SelenideElement counterEdited = $(".update-document__edited");

        return counterEdited.is(Condition.exist) ? counterEdited.getText() : "";
    }

    public String getCounterCommented()
    {
        SelenideElement counterCommented = $(".update-document__commented");

        return counterCommented.is(Condition.exist) ? counterCommented.getText() : "";
    }

    public String getCounterDeleted()
    {
        SelenideElement counterDeleted = $(".update-document__deleted");

        return counterDeleted.is(Condition.exist) ? counterDeleted.getText() : "";
    }

    /**
     * Click Upload button. If contract is in classic mode then it will show DISCUSSIONS tab, otherwise - opened contract
     * @param isClassic
     * @return DiscussionsOfSingleContract if contract is in classic mode, null otherwise
     */
    public DiscussionsOfSingleContract clickUpload(boolean isClassic)
    {
        uploadButton.shouldBe(Condition.visible).click();
        logger.info("UPLOAD button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 60_000 * 5);
        try { Thread.sleep(2_000); } catch (InterruptedException e) { e.printStackTrace(); }

        if( isClassic )
        {
            return new DiscussionsOfSingleContract(contractName);
        }

        return null;
    }
}

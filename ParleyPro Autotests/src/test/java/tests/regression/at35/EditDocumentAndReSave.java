package tests.regression.at35;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.EditDocumentPage;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class EditDocumentAndReSave
{
    private static Logger logger = Logger.getLogger(EditDocumentAndReSave.class);

    @Test
    @Description("This test edits document, insert empty table, saves document, edit again and saves again")
    public void editDocumentAndReSave()
    {
        OpenedContract openedContract = new OpenedContract();

        // Edit document for the first time
        EditDocumentPage editDocumentPage = openedContract.clickEditDocument("AT-14");

        editDocumentPage.insertTable().clickOk();

        logger.info("Assert that Table was added in Edit document view...");
        $(".editor-popup-editor__body table").waitUntil(Condition.visible, 7_000); // wait until table is visible
        boolean tableDoesntExist = Selenide.executeJavaScript("return ($('.editor-popup-editor__body table').length === 0)");
        Assert.assertFalse(tableDoesntExist);

        editDocumentPage.clickSave();

        logger.info("Assert that Table was added in main document after saving");
        $(".document-paragraph__content-text table").waitUntil(Condition.visible, 15_000);
        $$(".document-paragraph__content-text table").shouldHave(CollectionCondition.size(4));

        // Edit again
        editDocumentPage = openedContract.clickEditDocument("AT-14");

        // Save again
        editDocumentPage.clickSave();

        logger.info("Assert that table is visible after re-saving...");
        $(".document-paragraph__content-text table").waitUntil(Condition.visible, 15_000);
        $$(".document-paragraph__content-text table").shouldHave(CollectionCondition.size(4));

        Screenshoter.makeScreenshot();
    }
}

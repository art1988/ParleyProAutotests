package tests.basics.at61;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class DeleteAndCancelDocuments
{
    private static Logger logger = Logger.getLogger(DeleteAndCancelDocuments.class);

    @Test
    @Description("This test deletes document by the name 'Formatting' and cancel document by the name 'AT-40'")
    public void deleteAndCancelDocuments()
    {
        OpenedContract openedContract = new OpenedContract();

        logger.info("Trying to delete document [Formatting]...");
        openedContract.clickDocumentActionsMenu("Formatting").clickDelete().clickDelete();

        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.exactText("Document Formatting has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);

        logger.info("Making sure that only one document left...");
        $$(".rename.document__header-rename").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("AT-40"));

        Screenshoter.makeScreenshot();

        logger.info("Trying to cancel document [AT-40]...");
        openedContract.clickDocumentActionsMenu("AT-40").clickCancel().clickCancel();

        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.exactText("Document AT-40 has been cancelled."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);

        logger.info("Making sure that status of document was changed to CANCELLED...");
        $(".document .lifecycle").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("CANCELLED"));

        // TODO: add check that there is no Cancel option after cancelling after fixing of PAR-13248

        Screenshoter.makeScreenshot();
    }
}

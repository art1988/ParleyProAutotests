package tests.formatting.at190;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.Cache;
import utils.Screenshoter;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class UploadEditedDocAndCheckBullets
{
    private static Logger logger = Logger.getLogger(UploadEditedDocAndCheckBullets.class);

    @Test(priority = 1)
    @Description("This test uploads edited doc and checks that bulleted items have text.")
    public void uploadEditedDocAndCheckBullets()
    {
        logger.info("Uploading edited doc...");
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("IEEE Bullets CTR");

        new AddDocuments()
                .clickUploadMyTeamDocuments(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + Cache.getInstance().getCachedFile()));

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        $$(".lifecycle__item.first").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        logger.info("Traverse through all bulleted items and check presence of text...");
        $$(".document-paragraph__content-text span[list-item='true']").filter(Condition.exactText("•"))
                .forEach(span ->
                {
                    String textInsideP = span.closest("p").getText();
                    String textWithoutBullet = textInsideP.substring(textInsideP.indexOf("•") + 1).trim();
                    Assert.assertTrue(textWithoutBullet.length() > 5, "Looks like that some bulleted items are empty !!!");
                });

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}

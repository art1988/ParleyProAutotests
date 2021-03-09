package tests.regression.at75;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

@Listeners({ScreenShotOnFailListener.class})
public class CheckNumbering
{
    private static Logger logger = Logger.getLogger(CheckNumbering.class);

    @Test
    public void checkNumbering()
    {
        // TODO: add additional check ( that 'i' item starts from correct paragraph ) after fixing of PAR-13741
        logger.info("Scroll to necessary paragraph...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"[ORGANIZATION] will provide IEEE\")')[0].scrollIntoView({});");

        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text ins:contains(\"H.\")').parent().text().trim()"), "H.Test??");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text ins:contains(\"iii.\")').parent().text().trim().replace(/\\s/g, '')"), "iii.Yo!!!");

        Screenshoter.makeScreenshot();
    }
}

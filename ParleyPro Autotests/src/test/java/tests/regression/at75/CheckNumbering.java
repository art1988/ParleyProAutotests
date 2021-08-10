package tests.regression.at75;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
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
    @Issues({
            @Issue("PAR-14430"),
            @Issue("PAR-14445"),
            @Issue("PAR-14624")
    })
    public void checkNumbering()
    {
        logger.info("Scroll to necessary paragraph...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"[ORGANIZATION] will provide IEEE\")')[0].scrollIntoView({});");

        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text ins:contains(\"F.\")').parent().text().replace(/\\n/g,\"\").replace(/\\s/g, '')"), "F.Test!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text ins:contains(\"H.\")').parent().text().replace(/\\n/g,\"\").replace(/\\s/g, '')"), "H.Test??");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text ins:contains(\"ii.\")').parent().text().trim().replace(/\\s/g, '')"), "ii.Yo!!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text ins:contains(\"v.\")').parent().text().trim().replace(/\\s/g, '')"), "v.Yo!");

        Screenshoter.makeScreenshot();
    }
}

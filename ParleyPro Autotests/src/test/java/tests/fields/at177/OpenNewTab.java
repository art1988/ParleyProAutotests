package tests.fields.at177;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import utils.Cache;

import java.util.Set;


public class OpenNewTab
{
    private static WebDriver driver;
    private static Logger logger = Logger.getLogger(OpenNewTab.class);

    @Test(priority = 1)
    @Description("Opens new tab, saves tab's handles.")
    public void openNewTab()
    {
        driver = WebDriverRunner.getWebDriver();
        String currentHandle = driver.getWindowHandle();
        Cache.getInstance().setCurrentTabHandle(currentHandle);

        logger.info("Open new tab...");
        Selenide.executeJavaScript("window.open()");

        Set<String> handles = driver.getWindowHandles();
        for ( String actual : handles )
        {
            if ( !actual.equalsIgnoreCase(currentHandle) )
            {
                //switching to the opened tab
                driver.switchTo().window(actual);
                Cache.getInstance().setTab1Handle(actual);
            }
        }
    }
}

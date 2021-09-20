package utils;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Util class that helps to make screenshots at any place of test execution.
 * Also it attaches screenshot for Allure reports.
 */
public class Screenshoter
{
    private static Logger logger = Logger.getLogger(Screenshoter.class);


    @Attachment
    public static byte[] makeScreenshot()
    {
        File screenshot = Screenshots.takeScreenShotAsFile();
        byte[] result = null;

        try
        {
            result = Files.toByteArray(screenshot);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }

        logger.info("Screenshot was captured");

        return result;
    }
}

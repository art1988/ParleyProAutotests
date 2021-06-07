package utils;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    /**
     * Makes screenshot of a whole screen. May be useful to see opened tabs of browser window, bottom panel of OS, etc.
     */
    @Attachment
    public static byte[] makeScreenshotOfWholeScreen()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);

        Robot robot = null;
        try
        {
            robot = new Robot();
        }
        catch (AWTException e)
        {
            logger.error("AWTException: can't initialize Robot.", e);
        }

        BufferedImage image = robot.createScreenCapture(screenRectangle);
        File imgAsFile = new File("img.png");

        try
        {
            imgAsFile.createNewFile();
        }
        catch (IOException e)
        {
            logger.error("Unable to create file", e);
        }

        byte[] result = null;

        try
        {
            if( ImageIO.write(image, "png", imgAsFile) )
            {
                result = Files.toByteArray(imgAsFile);
            }
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }

        logger.info("Screenshot of whole screen was captured");

        return result;
    }
}

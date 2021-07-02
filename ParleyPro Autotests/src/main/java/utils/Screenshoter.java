package utils;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
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
/*

    @Attachment
    public static byte[] makeScreenshotOfWholeScreen()
    {
        Robot robot = null;
        try
        {
            robot = new Robot();
        }
        catch (AWTException e)
        {
            logger.error("AWTException: can't initialize Robot.", e);
        }

        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
        robot.delay(40);
        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
        robot.delay(40);

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        DataFlavor[] flavors = cb.getAvailableDataFlavors();

        flavors[0].


        BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        File imgAsFile = new File("img.jpg");

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
            if( ImageIO.write(image, "jpg", imgAsFile) )
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
    } */
}

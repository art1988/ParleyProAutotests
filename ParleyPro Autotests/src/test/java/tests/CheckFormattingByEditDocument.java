package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.testng.ScreenShooter;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.EditDocumentPage;
import pages.OpenedContract;
import utils.Screenshoter;

@Listeners({ ScreenShooter.class})
public class CheckFormattingByEditDocument
{
    private static Logger logger = Logger.getLogger(CheckFormattingByEditDocument.class);

    @Test
    @Description("This test checks that all styles of paragraphs are saved after uploading into ParleyPro")
    public void checkFormattingByEditDocument()
    {
        OpenedContract openedContract = new OpenedContract();

        EditDocumentPage editDocumentPage = openedContract.clickEditDocument("Formatting");

        String stringToCheck = "Times new roman bold 14 size";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "\"Times New Roman\""); // check font-family
        Assert.assertEquals(getFontWeight(stringToCheck), "700"); // check boldness; 700 is 'bold'
        Assert.assertEquals(getFontSize(stringToCheck), "18.6667px"); // check font-size; represents 14pt

        stringToCheck = "Times new roman italic 12 size";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "\"Times New Roman\""); // check font-family
        Assert.assertEquals(getFontStyle(stringToCheck), "italic"); // check font-style
        Assert.assertEquals(getFontSize(stringToCheck), "16px"); // check font-size; represents 12pt

        stringToCheck = "Arial underline 1";
        logger.info("Assert Arial underline 16 size");
        Assert.assertEquals(getFontFamily(stringToCheck), "Arial"); // check font-family
        String textDecoration = Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + stringToCheck + "\")').css(\"text-decoration\")");
        Assert.assertEquals(textDecoration, "underline solid rgb(52, 52, 52)"); // check underline, i.e. text-decoration
        Assert.assertEquals(getFontSize(stringToCheck), "21.3333px"); // check font-size; represents 16pt

        stringToCheck = "Times new roman regular 14 centered";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "\"Times New Roman\"");
        Assert.assertEquals(getFontSize(stringToCheck), "18.6667px"); // check font-size; represents 14pt
        Assert.assertEquals(getTextAlign(stringToCheck), "center"); // check text align
        Assert.assertEquals(getFontStyle(stringToCheck), "normal"); // check font-style

        stringToCheck = "Align left Calibri 15";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "Calibri");
        Assert.assertEquals(getFontSize(stringToCheck), "20px"); // check font-size; represents 15pt
        Assert.assertEquals(getTextAlign(stringToCheck), "start"); // check text align

        stringToCheck = "Align right Tahoma 14";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "Tahoma");
        Assert.assertEquals(getFontSize(stringToCheck), "18.6667px"); // check font-size; represents 14pt
        Assert.assertEquals(getTextAlign(stringToCheck), "right"); // check text align

        stringToCheck = "CALIBRI 15 CENTERED";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "Calibri");
        Assert.assertEquals(getFontSize(stringToCheck), "20px"); // check font-size; represents 15pt
        Assert.assertEquals(getTextAlign(stringToCheck), "center"); // check text align
        Assert.assertEquals(getFontWeight(stringToCheck), "700"); // check boldness; 700 is 'bold'

        stringToCheck = "Heading style times new roman 14"; // no Heading style for this string => <p> is parent
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "\"Times New Roman\"");
        Assert.assertEquals(getFontSize(stringToCheck), "18.6667px"); // check font-size; represents 14pt
        Assert.assertEquals(getParentTagName(stringToCheck), "P"); // check parent tag name

        stringToCheck = "Heading style calibri light 16"; // H1 Heading style
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "\"Calibri Light\"");
        Assert.assertEquals(getFontSize(stringToCheck), "21.3333px"); // check font-size; represents 16pt
        Assert.assertEquals(getParentTagName(stringToCheck), "H1"); // check parent tag name
        String color = Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + stringToCheck + "\")').css(\"color\")");
        Assert.assertEquals(color, "rgb(46, 116, 181)"); // check color; Should be blue

        stringToCheck = "Calibri 15 regular justify";
        logger.info("Assert " + stringToCheck);
        Assert.assertEquals(getFontFamily(stringToCheck), "Calibri");
        Assert.assertEquals(getFontSize(stringToCheck), "20px"); // check font-size; represents 15pt
        Assert.assertEquals(getFontStyle(stringToCheck), "normal");
        Assert.assertEquals(getTextAlign(stringToCheck), "justify"); // check text align

        Screenshoter.makeScreenshot();

        editDocumentPage.clickCancel();
    }

    /**
     * Get font-family css value of the given string
     * @param string
     * @return
     */
    private String getFontFamily(String string)
    {
        return Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + string + "\")').css(\"font-family\")");
    }

    /**
     * Get font-weight css value of the given string.
     * 700 = bold;
     * @param string
     * @return
     */
    private String getFontWeight(String string)
    {
        return Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + string + "\")').css(\"font-weight\")");
    }

    /**
     * Get font-size css value of the given string.
     * @param string
     * @return
     */
    private String getFontSize(String string)
    {
        return Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + string + "\")').css(\"font-size\")");
    }

    /**
     * Get font-style css value of the given string.
     * @param string
     * @return may be "normal" (regular), "italic"
     */
    private String getFontStyle(String string)
    {
        return Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + string + "\")').css(\"font-style\")");
    }

    /**
     * Get text-align css value of the given string.
     * @param string
     * @return "start" in case of left align; "right" in case of right align; or "justify"
     */
    private String getTextAlign(String string)
    {
        return Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + string + "\")').css(\"text-align\")");
    }

    /**
     * Get tag name of parent element of the given string. Use this method to determine if it has h1/h2... headings
     * @param string
     * @return
     */
    private String getParentTagName(String string)
    {
        return Selenide.executeJavaScript("return $('.editor-popup__body span:contains(\"" + string + "\")').parent().get(0).tagName");
    }
}

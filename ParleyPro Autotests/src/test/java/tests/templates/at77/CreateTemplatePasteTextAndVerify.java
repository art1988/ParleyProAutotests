package tests.templates.at77;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateTemplatePasteTextAndVerify
{
    private String templateName = "Template_AT-77_dummy";
    private String textToPaste = "\\n" +
            "<<[contract.getCustomFields().get(“Borrower5”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower6\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower6”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower7\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower7”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower8\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower8”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower9\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower9”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower10\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower10”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower11\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower11”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower12\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower12”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower13\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower13”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower14\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower14”)]>><</if>><<if[contract.getCustomFields().get(\"Borrower15\")!=null]>>\\n" +
            "\\n" +
            "\\n" +
            "\\n" +
            "<<[contract.getCustomFields().get(“Borrower15”)]>><</if>>\\n";

    private Logger logger = Logger.getLogger(CreateTemplatePasteTextAndVerify.class);

    @Test(priority = 1)
    @Description("This test uploads template and paste text at the beginning")
    public void uploadDocAsTemplateAndPasteText() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);

        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT77 );

        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.exactText("Template Template_AT-77_dummy was added."));
        $(".notification-stack .notification__close").click();

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate(templateName);

        editTemplatePage.addText(textToPaste);
        editTemplatePage.clickPublishButton();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test reopens just added template and verifies that text was added.")
    public void reopenTemplateAndVerify()
    {
        EditTemplatePage editTemplatePage = new DashboardPage().getSideBar().clickTemplates(false).selectTemplate(templateName);

        logger.info("Assert that template has text...");
        String textInsideOfTemplate = editTemplatePage.getText();
        Assert.assertEquals(textInsideOfTemplate, "<<[contract.getCustomFields().get(“Borrower5”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower6\")!=null]>><<[contract.getCustomFields().get(“Borrower6”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower7\")!=null]>><<[contract.getCustomFields().get(“Borrower7”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower8\")!=null]>><<[contract.getCustomFields().get(“Borrower8”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower9\")!=null]>><<[contract.getCustomFields().get(“Borrower9”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower10\")!=null]>><<[contract.getCustomFields().get(“Borrower10”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower11\")!=null]>><<[contract.getCustomFields().get(“Borrower11”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower12\")!=null]>><<[contract.getCustomFields().get(“Borrower12”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower13\")!=null]>><<[contract.getCustomFields().get(“Borrower13”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower14\")!=null]>><<[contract.getCustomFields().get(“Borrower14”)]>><</if>>" +
                "<<if[contract.getCustomFields().get(\"Borrower15\")!=null]>><<[contract.getCustomFields().get(“Borrower15”)]>><</if>>\u200B \u200BThis is dummy PDF document. Please delete it after uploading legacy contract documents.");

        Screenshoter.makeScreenshot();

        editTemplatePage.clickCancelButton();
    }
}

package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.editor_toolbar.Field;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents selected template page in Edit mode where editor is available
 */
public class EditTemplatePage
{
    private SelenideElement publishButton = $(".button.btn-small.btn.btn-primary");



    private static Logger logger = Logger.getLogger(EditTemplatePage.class);

    public EditTemplatePage()
    {
        $(".spinner").waitUntil(Condition.disappear, 10_000);

        $(".rename > span").waitUntil(Condition.visible, 10_000);
        $(".cke_inner").waitUntil(Condition.visible, 10_000);
    }

    /**
     * Click by 'Add a smart field' button and choose type
     * @param smartFieldType may be: Contract name, Contract due date, Contract category, Contract region etc.
     */
    public void addSmartField(String smartFieldType) throws InterruptedException
    {
        // click on 'Add a smart field' button
        $("#editor-toolbar a[title='Add a smart field']").waitUntil(Condition.visible, 7__000).click();

        // Choose type
        Selenide.executeJavaScript("$($('.cke_combopanel > iframe')[0].contentDocument).find(\".cke_panel_list a:contains('" + smartFieldType + "')\").click()");

        Thread.sleep(500);
    }

    public Field addField()
    {
        $("#editor-toolbar a[title='Field']").waitUntil(Condition.visible, 7__000).click();

        return new Field();
    }

    public void clickPublishButton()
    {
        publishButton.click();

        logger.info("PUBLISH button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }
}

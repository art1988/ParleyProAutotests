package tests.templates.at207;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddTemplateAndSearch
{
    private static final String TEMPLATE_NAME = "Template_AT48";
    private TemplatesPage templatesPage;


    private static Logger logger = Logger.getLogger(AddTemplateAndSearch.class);


    @Test(priority = 1)
    public void addTemplate()
    {
        templatesPage = new DashboardPage().getSideBar().clickTemplates();
        templatesPage.clickNewTemplate().clickUploadTemplatesButton(Const.TEMPLATE_AT48);

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" was added."));
        Selenide.refresh();

        $$(".template__title").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText(TEMPLATE_NAME));
    }

    @Test(priority = 2)
    public void search()
    {
        logger.info("Search non-existing template...");
        templatesPage.search("ABC");
        $(".templates-board__empty").shouldBe(Condition.visible).shouldHave(Condition.text("There are no templates matching \"ABC\""));

        $(".templates-search__close").click(); // clear search field

        logger.info("Search " + TEMPLATE_NAME + " template...");
        templatesPage.search(TEMPLATE_NAME);
        $$(".template__title").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText(TEMPLATE_NAME));

        Screenshoter.makeScreenshot();
    }
}

package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.add.AddNewTeam;
import forms.delete.DeleteTeam;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Class that represents tooltip with Edit and Delete buttons
 */
public class TeamActionMenu
{
    private static Logger logger = Logger.getLogger(TeamActionMenu.class);

    public TeamActionMenu()
    {
        Selenide.sleep(1_000);
        $(".dropdown.open.btn-group ul").shouldBe(Condition.visible);
        $$(".dropdown.open.btn-group ul li").shouldHaveSize(3).shouldHave(CollectionCondition.exactTexts("Edit", "", "Delete"));
    }

    public AddNewTeam clickEdit()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Edit\")').find('a')[0].click()");

        logger.info("Edit team menu was clicked...");

        return new AddNewTeam();
    }

    public DeleteTeam clickDelete()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Delete\")').find('a')[0].click()");

        logger.info("Delete team menu was clicked...");

        return new DeleteTeam();
    }
}

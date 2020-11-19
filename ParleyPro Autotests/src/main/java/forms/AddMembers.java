package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class AddMembers
{
    private SelenideElement addParticipantField = $("#teamsAddParticipant");
    private SelenideElement addButton           = $("._button.scheme_blue.size_md");


    private static Logger logger = Logger.getLogger(AddMembers.class);

    public AddMembers()
    {
        $(".modal-header").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Add members"));
    }

    /**
     * Click by Add participant dropdown and add new member
     * @param nameOrEmail
     */
    public void addParticipant(String nameOrEmail)
    {
        addParticipantField.setValue(nameOrEmail);
        addParticipantField.sendKeys(Keys.DOWN);
        addParticipantField.sendKeys(Keys.ENTER);

        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }
    }

    public void clickAdd()
    {
        addButton.click();

        logger.info("Add button was clicked");
    }
}

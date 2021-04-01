package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import forms.ManageDiscussions;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * Class that represents list of posts in Manage Discussions form that appears after clicking on
 * arrow icon for internal, queued or external.
 */
public class ListOfPosts
{
    private SelenideElement title = $(".modal-header");
    private SelenideElement selectAllCheckbox;

    private SelenideElement discardButton,
                            acceptButton,
                            makeQueuedButton,
                            makeExternalButton;

    private SelenideElement arrowBack = $(".manage-discussions-external-posts__back");

    private static Logger logger = Logger.getLogger(ListOfPosts.class);


    public ListOfPosts(String groupType)
    {
        title.waitUntil(Condition.visible, 7_000);

        if( groupType.equals("internal") )
        {

        }
        else if( groupType.equals("queued") )
        {

        }
        else if( groupType.equals("external") )
        {
            Assert.assertTrue(title.getText().endsWith("external discussions"), "Looks like that title doesn't end with external discussions !!!");
        }
        else
        {
            logger.error("The following group type doesn't exists ! " + groupType);
        }
    }

    public ManageDiscussions clickBack()
    {
        arrowBack.click();

        logger.info("Back to ManageDiscussions was clicked");

        return new ManageDiscussions();
    }
}

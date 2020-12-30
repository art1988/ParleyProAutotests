package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represent tooltip that appears after clicking on Role for internal users in SHARE form.
 * Allows to select certain role.
 */
public class RoleSelectorTooltip
{
    private String username;


    private static Logger logger = Logger.getLogger(RoleSelectorTooltip.class);

    public RoleSelectorTooltip(String username)
    {
        this.username = username;

        $(".active ul").waitUntil(Condition.visible, 5_000);
        $$(".active ul li").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Lead", "Reviewer"));
    }

    public void setLeadRole()
    {
        $$(".active ul li").filter(Condition.exactText("Lead")).first().click();

        logger.info("Lead role was set for user: " + username);
    }

    public void setReviewerRole()
    {
        $$(".active ul li").filter(Condition.exactText("Reviewer")).first().click();

        logger.info("Reviewer role was set for user: " + username);
    }
}

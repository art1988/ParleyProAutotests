package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class ClauseLibrary
{
    private static Logger logger = Logger.getLogger(ClauseLibrary.class);


    public ClauseLibrary()
    {
        $(".modal-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Clause library"));
        $(".discussion2-answers-list").shouldBe(Condition.visible);
    }

    /**
     * Click by blue checkmark button (✓) of given clause title
     * @param clauseTitle
     */
    public void acceptClause(String clauseTitle)
    {
        Selenide.executeJavaScript("$('.discussion2-answers-list__title:contains(\"" + clauseTitle + "\")').parent().find('span').click()");

        logger.info(clauseTitle + " has been accepted.");

        $(".spinner").should(Condition.disappear);
    }
}

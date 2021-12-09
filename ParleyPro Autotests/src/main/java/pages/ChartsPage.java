package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents Dashboard page ( pie chart icon )
 */
public class ChartsPage
{
    private static Logger logger = Logger.getLogger(ChartsPage.class);


    public ChartsPage()
    {
        $(".page-head").shouldBe(Condition.visible).shouldHave(Condition.exactText("Dashboard"));

        $(".spinner").should(Condition.disappear);

        $$(".tab-menu .tab-menu__item").shouldHave(CollectionCondition.size(2))
                                                 .shouldHave(CollectionCondition.textsInAnyOrder("IN-PROGRESS CONTRACTS", "EXECUTED CONTRACTS"));
    }

    public ChartsPage clickInProgressContractsTab()
    {
        $$(".tab-menu .tab-menu__item").filterBy(Condition.exactText("IN-PROGRESS CONTRACTS")).first().click();

        logger.info("IN-PROGRESS CONTRACTS tab was clicked");

        return this;
    }

    public ChartsPage clickExecutedContractsTab()
    {
        $$(".tab-menu .tab-menu__item").filterBy(Condition.exactText("EXECUTED CONTRACTS")).first().click();

        logger.info("EXECUTED CONTRACTS tab was clicked");

        return this;
    }
}

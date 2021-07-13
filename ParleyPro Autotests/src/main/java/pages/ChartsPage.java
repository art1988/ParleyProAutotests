package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents Dashboard page ( pie chart icon )
 */
public class ChartsPage
{
    public ChartsPage()
    {
        $(".page-head").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Dashboard"));

        $(".spinner").waitUntil(Condition.disappear, 25_000);

        $$(".tab-menu .tab-menu__item").shouldHave(CollectionCondition.size(2))
                                                 .shouldHave(CollectionCondition.textsInAnyOrder("IN-PROGRESS CONTRACTS", "EXECUTED CONTRACTS"));
    }
}

package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents Priority Dashboard Page ( star icon )
 */
public class PriorityDashboardPage
{
    public PriorityDashboardPage()
    {
        $(".page-head").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Priorities"));

        $(".spinner").waitUntil(Condition.disappear, 25_000);

        $$(".priority-dashboard__title").shouldHave(CollectionCondition.size(2))
                                                  .shouldHave(CollectionCondition.textsInAnyOrder("My priorities", "Company’s priorities"));
    }
}

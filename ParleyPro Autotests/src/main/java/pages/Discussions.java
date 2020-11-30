package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents opened DISCUSSIONS tab within contract
 */
public class Discussions
{
    private SelenideElement contractTitle = $(".contract-header__name");


    public Discussions(String contractName)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        contractTitle.waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(contractName));
    }

    public String getDiscussionCount()
    {
        return $(".discussions-info .discussion-indicator__count").getText();
    }
}

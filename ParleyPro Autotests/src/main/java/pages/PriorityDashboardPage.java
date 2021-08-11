package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ContractInformation;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents Priority Dashboard Page ( star icon )
 */
public class PriorityDashboardPage
{
    private SelenideElement newContractButtonForInProgress = $(".js-create-active-contract");
    private SelenideElement newContractButtonForExecuted   = $(".js-create-executed-contract");

    private Logger logger = Logger.getLogger(PriorityDashboardPage.class);

    public PriorityDashboardPage()
    {
        $(".page-head").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Priorities"));

        $(".spinner").waitUntil(Condition.disappear, 25_000);

        $$(".priority-dashboard__title").shouldHave(CollectionCondition.size(2))
                                                  .shouldHave(CollectionCondition.textsInAnyOrder("My priorities", "Company’s priorities"));
    }

    public ContractInformation clickNewContractButtonForInProgress()
    {
        newContractButtonForInProgress.shouldBe(Condition.visible).click();

        logger.info("+ NEW CONTRACT button was clicked for 'All in-progress contracts'...");

        return new ContractInformation();
    }

    public String getCountOfAllInProgressContracts()
    {
        return Selenide.executeJavaScript("return $('.priority-item__title:contains(\"in-progress\")').parent().parent().find(\".priority-item__value\").text().trim()");
    }
}

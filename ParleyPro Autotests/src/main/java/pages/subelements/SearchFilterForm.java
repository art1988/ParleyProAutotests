package pages.subelements;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents Search filter form that appears after clicking ▼ button of search input
 */
public class SearchFilterForm
{
    private static Logger logger = Logger.getLogger(SearchFilterForm.class);


    public SearchFilterForm()
    {
        $(".contracts-search__body .spinner").should(Condition.disappear);
        $$(".contracts-search__foot button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Reset", "Search"));
    }

    public SearchFilterForm setRegion(String regionToSearch)
    {
        $("input[data-label='Region']").click(); // expand
        $(".contracts-search__form .dropdown-menu[open]").findAll("span").filterBy(Condition.text(regionToSearch)).first().click();
        logger.info(regionToSearch + " was selected");
        $("input[data-label='Region']").click(); // collapse

        return this;
    }

    public SearchFilterForm setCustomFieldVal(String fieldName, String valueToSearch)
    {
        SelenideElement input = $$(".input__label-title").filterBy(Condition.text(fieldName)).first().closest("div").find("input");

        input.click(); // expand
        $(".contracts-search__form .dropdown-menu[open]").findAll("span").filterBy(Condition.text(valueToSearch)).first().click();
        logger.info(valueToSearch + " was selected");
        input.click(); // collapse

        return this;
    }

    public SearchFilterForm setContractNegotiator(String cnToSearch)
    {
        SelenideElement input = $$(".input__label-title").filterBy(Condition.text("Contract Negotiator")).first().closest("div").find(".new-select__control");

        input.click(); // expand
        $$(".new-select__menu .new-select__option").filterBy(Condition.text(cnToSearch)).first().click();
        logger.info(cnToSearch + " was selected");

        return this;
    }

    public void clickSearch()
    {
        $$(".contracts-search__foot button").filterBy(Condition.text("Search")).first().click();
        logger.info("SEARCH button was clicked...");
        $(".spinner").should(Condition.disappear);
    }
}

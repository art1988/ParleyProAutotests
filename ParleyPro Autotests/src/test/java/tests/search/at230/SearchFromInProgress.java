package tests.search.at230;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import pages.InProgressContractsPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class SearchFromInProgress
{
    private SideBar sideBar;
    private InProgressContractsPage inProgressContractsPage;

    @Test
    public void searchFromInProgress()
    {
        sideBar = new DashboardPage().getSideBar();

        searchByTitle();
        searchByContractingRegion();
        searchByCustomField();
        searchByContractNegotiator();
        searchByNotes();
        searchByClassicNegotiation();
        searchByContractValue();
    }

    @Step("Search by contract title = contract")
    public void searchByTitle()
    {
        inProgressContractsPage = sideBar.clickInProgressContracts(false);

        inProgressContractsPage.search("contract");
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Normal contract", "Online Contract"));
        $(".contracts-tabs a[class*='ui']").shouldBe(Condition.visible).shouldHave(Condition.text("1 Executed contract"));
        Selenide.executeJavaScript("$('.contracts-tabs a[class*=\"ui\"]')[0].click()"); // click by '1 Executed contract' link. Other clicks doesn't work

        new ExecutedContractsPage(false);
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Normal values in contract"));
        $(".contracts-tabs a[class*='ui']").shouldBe(Condition.visible).shouldHave(Condition.text("2 In-progress contracts")).click();
        $(".contracts-search-input__close").click(); // clear previous search
    }

    @Step("Search by contracting region = 1")
    public void searchByContractingRegion()
    {
        inProgressContractsPage.expandSearchFilter().setRegion("1").clickSearch();
        $$(".contracts-search-input__rule").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Region"));
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Classic"));
        $(".contracts-tabs a[class*='ui']").shouldBe(Condition.visible).shouldHave(Condition.text("1 Executed contract"));
        $(".contracts-search-input__close").click(); // clear previous search
    }

    @Step("Search by custom field ‘Some Multi-Select Field’ = A very long")
    public void searchByCustomField()
    {
        inProgressContractsPage.expandSearchFilter().setCustomFieldVal("Some Multi-Select Field", "A very long").clickSearch();
        $$(".contracts-search-input__rule").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Some Multi-Select Field"));
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Online Contract One With a Long Long"));
        $(".contracts-tabs a[class*='ui']").shouldBe(Condition.visible).shouldHave(Condition.text("1 Executed contract"));
        $(".contracts-search-input__close").click(); // clear previous search
    }

    @Step("Search by Contract Negotiator field = yevhen.uvin+at50@parleypro.com")
    public void searchByContractNegotiator()
    {
        inProgressContractsPage.expandSearchFilter().setContractNegotiator("yevhen.uvin+at50@parleypro.com").clickSearch();
        $$(".contracts-search-input__rule").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Contract Negotiator"));
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("Classic", "Normal contract", "Online Contract One With a Long Long"));
        $(".contracts-tabs a[class*='ui']").shouldBe(Condition.visible).shouldHave(Condition.text("3 Executed contracts"));
        $(".contracts-search-input__close").click(); // clear previous search
    }

    @Step("Search by ‘Notes’ field = The volcano rises 2,100 metres")
    public void searchByNotes()
    {
        inProgressContractsPage.expandSearchFilter().setNotes("The volcano rises 2,100 metres").clickSearch();
        $$(".contracts-search-input__rule").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Notes"));
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Online Contract One With a Long"));
        $(".contracts-search-input__close").click(); // clear previous search
    }

    @Step("Search by Classic negotiation = Yes")
    public void searchByClassicNegotiation()
    {
        inProgressContractsPage.expandSearchFilter().setClassicNegotiation("Yes").clickSearch();
        $$(".contracts-search-input__rule").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Classic negotiation"));
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Classic"));
        $(".contracts-tabs a[class*='ui']").shouldBe(Condition.visible).shouldHave(Condition.text("2 Executed contracts"));
        $(".contracts-search-input__close").click(); // clear previous search
    }

    @Step("Search by ‘Contract value’ from 12000 to 13000")
    public void searchByContractValue()
    {
        inProgressContractsPage.expandSearchFilter().setContractValue("12000", "13000").clickSearch();
        $$(".contracts-search-input__rule").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Contract value"));
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Classic"));
    }
}

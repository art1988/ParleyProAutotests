package tests.regression.at150;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckContractsOnExecutedPage
{
    private static Logger logger = Logger.getLogger(CheckContractsOnExecutedPage.class);

    @Test
    @Description("This test hovers over linked icons and checks content in them.")
    public void visitExecutedContractsAndCheck()
    {
        ExecutedContractsPage executedContractsPage = new DashboardPage().getSideBar().clickExecutedContracts(false);

        logger.info("Checking that row for contract1 and for contract2 has link icons...");
        $$(".glyphicon.glyphicon-link").shouldHave(CollectionCondition.size(2));

        logger.info("Checking hover for contract1 row...");
        $$(".contracts-list__contract-name").filter(Condition.exactText("contract1"))
                .first().parent().parent().find(".glyphicon.glyphicon-link").hover();
        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Linked contracts: 1"));
        $(".js-linked-contracts-head").shouldHave(Condition.exactText("Related to:\ncontract2"));
        Screenshoter.makeScreenshot();

        Selenide.refresh(); // refresh page to get rid of previous opened popup via hover

        executedContractsPage = new ExecutedContractsPage(false);

        logger.info("Checking hover for contract2 row...");
        $$(".contracts-list__contract-name").filter(Condition.exactText("contract2"))
                .first().parent().parent().find(".glyphicon.glyphicon-link").hover();
        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Linked contracts: 1"));
        $(".js-linked-contracts-head").shouldHave(Condition.exactText("Related to:\ncontract1"));
        Screenshoter.makeScreenshot();
    }
}

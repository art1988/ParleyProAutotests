package tests.regression.at209;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Cache;

import static com.codeborne.selenide.Selenide.$$;


public class AsCCNAddExternalDiscussion
{
    private DashboardPage dashboardPage;

    private static Logger logger = Logger.getLogger(AsCCNAddExternalDiscussion.class);


    @Test(priority = 1)
    public void asCCNAddExternalDiscussion() throws InterruptedException
    {
        logger.info("Login as CCN...");

        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract(Cache.getInstance().getCachedContractTitle());

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickReadyForSignature().clickOk();

        openedContract.clickByParagraph("Paragraph 2")
                      .setText(". CCN from AT-209")
                      .selectExternal()
                      .clickPost("Paragraph 2: Create comment here. CCN from AT-209", "qa-autotests")
                      .clickPostExternally();

        // purple icon of new discussion is visible
        $$(".document__body .negotiating").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void logoutAsCCN()
    {
        dashboardPage.getSideBar().logout();
    }
}

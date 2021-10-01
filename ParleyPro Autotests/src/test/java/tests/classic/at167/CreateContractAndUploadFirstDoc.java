package tests.classic.at167;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import forms.SendInvitation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndUploadFirstDoc
{
    private String contractName = "AT-167 classic ctr";
    private OpenedContract openedContract;
    private static Logger logger = Logger.getLogger(CreateContractAndUploadFirstDoc.class);

    @Test(priority = 1)
    public void createContractAndUploadFirstDoc()
    {
        Cache.getInstance().setContractTitle(contractName);

        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contractName);
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformation.checkClassicNegotiationModeCheckbox();
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.CLASSIC_AT_167_V1 );

        $(".notification-stack").waitUntil(Condition.appear, 20_000);
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);
        new ContractInNegotiation(contractName).clickOk();

        openedContract = new OpenedContract();

        logger.info("Making sure that 4 internal discussions have been created...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "4", "Amount of opened discussions is wrong !!!");
        $$(".document__body .discussion-indicator").shouldHave(CollectionCondition.size(4))
                .forEach(icon -> Assert.assertEquals(icon.getCssValue("color"), "rgba(25, 190, 155, 1)"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void makeExternalAndUploadSecondDoc()
    {
        openedContract.clickManageDiscussions().makeExternalAllInternalDiscussions().confirmAccept();

        new SendInvitation(contractName).clickStart();
        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.text("is now in negotiation"));

        // wait until color of discussion's icons become purple
        $(".contract-header__status .discussion-indicator").waitUntil(Condition.cssValue("color", "rgba(127, 111, 207, 1)"), 20_000);
        $$(".document__body .discussion-indicator").first().waitUntil(Condition.cssValue("color", "rgba(127, 111, 207, 1)"), 20_000);

        logger.info("Making sure that all discussions become external ...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "4", "Amount of opened discussions is wrong !!!");
        $$(".document__body .discussion-indicator").shouldHave(CollectionCondition.size(4))
                .forEach(icon -> Assert.assertEquals(icon.getCssValue("color"), "rgba(127, 111, 207, 1)"));

        Screenshoter.makeScreenshot();
    }
}

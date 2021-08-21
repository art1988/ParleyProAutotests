package tests.classic.at139;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndUploadFirstDoc
{
    private static Logger logger = Logger.getLogger(CreateContractAndUploadFirstDoc.class);

    @Test(priority = 1)
    public void сreateContractAndUploadFirstDoc()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("AT-139 Microvention doc");
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

        new AddDocuments().clickUploadMyTeamDocumentsWithDetectedChanges( Const.DOC_MICROVENTION_AT139_ONE ).clickOk();
        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.text(" has been successfully uploaded."));

        OpenedContract openedContract = new OpenedContract();

        logger.info("Checking Negotiate stage... Checking that only 1 discussion was created and it has 2 posts...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "1", "Amount of discussions is wrong !!! Should be 1.");
        $$(".document__body-content .discussion-indicator.negotiating").shouldHave(CollectionCondition.size(1));

        Selenide.refresh();
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Sales Schema\")')");

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon("Sales Schema");
        Assert.assertEquals(openedDiscussion.getCountOfPosts(), "2", "Amount of posts is wrong !!! Should be 2.");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Client hereby authorizes", "Client’s individual LinkedIn profile"));

        Screenshoter.makeScreenshot();
    }
}

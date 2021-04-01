package tests.regression.at111;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.ManageDiscussions;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import pages.subelements.ListOfPosts;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocAndMakePosts
{
    private String paragraph = "Paragraph 3: Insert something above me";
    private Logger logger = Logger.getLogger(CreateContractUploadDocAndMakePosts.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Grey screen in Manage Discussions");
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        new OpenedContract().switchDocumentToNegotiate("AT-14", "CounterpartyAT", false)
                            .clickNext(false)
                            .clickStart();

        $(".document__header-info .lifecycle").waitUntil(Condition.exactText("NEGOTIATE\nSIGN"), 7_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void addCommentAndPost() throws InterruptedException
    {
        new OpenedContract().hover(paragraph)
                            .clickAddComment()
                            .setComment("Simple comment ABC")
                            .selectExternal()
                            .clickPost(paragraph, "CounterpartyAT")
                            .clickPostExternally();

        $(".notification-stack").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("External discussion Paragraph 3: Insert something above me has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 14_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void makeChangesForTheSameParagraph() throws InterruptedException
    {
        new OpenedContract().clickByParagraph(paragraph)
                            .setText("change")
                            .setComment("Comment after changing")
                            .selectInternal()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Internal post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 14_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void openManageDiscussionsAndValidate()
    {
        logger.info("Making sure that Manage Discussions was opened and no grey screen occurred...");

        ManageDiscussions manageDiscussionsForm = new OpenedContract().clickManageDiscussions();

        Assert.assertEquals(manageDiscussionsForm.getAmountOfExternalDiscussions(), "1");
        Assert.assertEquals($(".manage-discussions-section__foot .manage-discussions-section__actions").getText(),
                   "DISCARD\nACCEPT\nMAKE QUEUED\nMAKE EXTERNAL");

        Screenshoter.makeScreenshot();

        ListOfPosts listOfPosts = manageDiscussionsForm.expandDiscussionGroup("external");

        logger.info("Making sure that only one post was added in list...");
        $$(".manage-discussions-post").shouldHave(CollectionCondition.size(1));
        Assert.assertEquals($(".manage-discussions-post .manage-discussions-post__text").getText().trim(),
                "Paragraph 3: Insert something above memechange",
                "Added post is not the same as on Manage Discussions form !!!");

        Screenshoter.makeScreenshot();

        manageDiscussionsForm = listOfPosts.clickBack();
        manageDiscussionsForm.clickDone();
    }
}

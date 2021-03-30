package tests.regression.at111;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocAndMakePosts
{
    private String paragraph = "Paragraph 3: Insert something above me";

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Grey screen - CCN");
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
        new OpenedContract().hover(paragraph)
                            .clickAddComment()
                            .setComment("Comment after changing")
                            .setText("change")
                            .selectInternal()
                            .clickPost();

        Thread.sleep(5_000);
    }
}

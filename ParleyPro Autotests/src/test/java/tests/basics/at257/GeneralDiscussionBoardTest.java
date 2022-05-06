package tests.basics.at257;

import com.codeborne.selenide.*;
import constants.AcceptTypes;
import constants.Const;
import forms.AcceptPost;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import forms.UploadDocumentDetectedChanges;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.Discussions;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class GeneralDiscussionBoardTest
{
    private static final String CONTRACT_NAME = "AT257 - GeneralDiscussionBoardTest";

    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(GeneralDiscussionBoardTest.class);


    @BeforeMethod
    public void createContractAndUploadDocs()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(CONTRACT_NAME);
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

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOC_AT257_MY_TEAM);
        new UploadDocumentDetectedChanges().clickOk();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Document AT-257-1_myTeam has been successfully uploaded"));

        openedContract = new OpenedContract();
        openedContract.clickNewDocument().clickUploadCounterpartyDocuments(Const.DOC_AT257_CP);
        new ContractInNegotiation(CONTRACT_NAME).clickOk();

        $$(".document__header-rename > span").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.textsInAnyOrder("AT-257-1_myTeam", "AT-257-2_CP"));
    }

    @Test
    public void generalDiscussionBoardTest()
    {
        sortDiscussionsColumn();
        setNonStandardTermForParagraph1AndHighPriorityTagForParagraph2();
        renameParagraph3();
        openParagraph4AndAcceptLastPost();
    }

    @Step
    public void sortDiscussionsColumn()
    {
        Discussions discussionsBoard = sideBar.clickInProgressContracts(false).clickDiscussionsTab();

        logger.info("Sort Discussions by name...");
        discussionsBoard.sortColumn("Discussions");
        Selenide.sleep(1_000);

        logger.info("Assert that sorting was applied...");
        ElementsCollection sortedParagraphs = $$(".discussion2_collapsed_yes div[class*=\"style__discussion\"]").shouldHave(CollectionCondition.size(8));
        for( int pNum = 0; pNum < sortedParagraphs.size(); pNum++ )
        {
            Assert.assertTrue(sortedParagraphs.get(pNum).getText().startsWith("Paragraph " + (pNum + 1)), "Looks like that Discussions column wasn't sorted !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Step
    public void setNonStandardTermForParagraph1AndHighPriorityTagForParagraph2()
    {
        ///
        logger.info("Set non-standard term for Paragraph 1...");
        $$(".discussion2_collapsed_yes div[class*=\"style__discussion\"]").filterBy(Condition.text("Paragraph 1")).first().parent().find("div[class*='style__terms']").find(".tumbler").click();
        $(".select__loading").should(Condition.disappear); // wait until inner spinner of input will disappear

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withLocale(Locale.US);
        String tagToAdd = "AT257 tag_" + LocalDateTime.now().format(formatter);
        $(".select__input.input__input").shouldBe(Condition.visible, Condition.enabled).sendKeys(tagToAdd);

        // Wait until popup with added tag is visible
        $("#react-autowhatever-1").waitUntil(Condition.visible, 5_000);
        Selenide.sleep(1_000);
        $$("#react-autowhatever-1 li").first().click();
        $(".spinner").waitUntil(Condition.disappear, 10_000);
        Selenide.sleep(1_000);
        $(".term-select__label").shouldBe(Condition.visible).shouldHave(Condition.exactText(tagToAdd));

        ///
        logger.info("Set high priority for Paragraph 2...");
        $$(".discussion2_collapsed_yes div[class*=\"style__discussion\"]").filterBy(Condition.text("Paragraph 2")).first().parent().find("div[class*='style__priority']").find(".label_priority").click();
        Selenide.sleep(1_000);
        Selenide.refresh();
        $$(".tumbler.checked.label_priority").shouldHave(CollectionCondition.size(1));
        Selenide.sleep(1_000);

        Screenshoter.makeScreenshot();
    }

    @Step
    public void renameParagraph3()
    {
        logger.info("Rename Paragraph3...");
        SelenideElement paragraph3 = $$(".discussion2_collapsed_yes div[class*=\"style__discussion\"]").filterBy(Condition.text("Paragraph 3")).first();
        paragraph3.hover();
        Selenide.sleep(1_000);
        Selenide.executeJavaScript("$('.rename__icon:visible').click()");
        $(".rename-form").shouldBe(Condition.visible);

        SelenideElement renameTextArea = $(".rename-form textarea");
        renameTextArea.sendKeys(renameTextArea.text() + " [EDITED]");
        $(".rename-form__buttons .scheme_blue").click();
        Selenide.sleep(1_000);

        Assert.assertTrue(paragraph3.getText().endsWith("[EDITED]"), "Looks like that Paragraph 3 has not been renamed !!!");
        Screenshoter.makeScreenshot();
    }

    @Step
    public void openParagraph4AndAcceptLastPost()
    {
        logger.info("Open Paragraph 4 and accept last post...");
        $$(".discussion2_collapsed_yes div[class*=\"style__discussion\"]").filterBy(Condition.text("Paragraph 4")).first().click();
        $(".discussion2-post__foot-action.accept").click(); // Click ACCEPT button
        new AcceptPost(AcceptTypes.ACCEPT).clickAcceptText();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("post has been successfully created"));
        $$(".accepted .discussion2-label__status").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("ACCEPTED"));

        Screenshoter.makeScreenshot();
    }
}

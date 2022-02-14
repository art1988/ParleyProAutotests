package tests.document_sharing.at227;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInformation;
import forms.ShareForm;
import forms.add.AddMembers;
import forms.add.AddNewTeam;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ShareWithTeamChangeTheTeam
{
    private SideBar sideBar;

    private static Logger logger = Logger.getLogger(ShareWithTeamChangeTheTeam.class);


    public void addTeamWithTwoUsers()
    {
        sideBar = new DashboardPage().getSideBar();

        logger.info("Adding team with 2 users...");
        AddNewTeam addNewTeam = sideBar.clickAdministration().clickTeamsTab().clickAddNewTeam();
        addNewTeam.setTeamName("TEAM1");

        AddMembers addMembers = addNewTeam.clickAddMembersButton();
        addMembers.addParticipant(Const.USER_MARY.getEmail());
        addMembers.addParticipant(Const.USER_GREG.getEmail());
        addMembers.clickAdd();

        addNewTeam.clickSave();
        $(byText("TEAM1")).shouldBe(visible);
    }

    public void addContract()
    {
        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-227 CNTR");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }

    @BeforeMethod(description = "Add TEAM1 with 2 users: Mary and Greg; Add contract and upload simple my team document")
    public void preSetup()
    {
        addTeamWithTwoUsers();
        addContract();
    }

    @Test
    public void shareWithTeam() throws InterruptedException
    {
        shareContractWithTEAM1();
        addOneMoreUserToTeam();
        returnToTheContractAndShareAgain();
    }

    @Step("Share the contract with TEAM1")
    public void shareContractWithTEAM1() throws InterruptedException
    {
        new OpenedContract().clickSHARE("AT-14").addParticipant("TEAM1").clickSend();

        logger.info("Check that team icon become visible...");
        $$(".team-icon").shouldHave(CollectionCondition.size(1)).first().shouldBe(visible);
    }

    @Step("Add one more user to TEAM1")
    public void addOneMoreUserToTeam()
    {
        logger.info("Adding one more user to team...");
        AddNewTeam addNewTeam = sideBar.clickAdministration().clickTeamsTab().clickActionMenu("TEAM1").clickEdit();
        AddMembers addMembers = addNewTeam.clickAddMembersButton();
        addMembers.addParticipant(Const.USER_FELIX.getEmail());
        addMembers.clickAdd();

        addNewTeam.clickSave();

        logger.info("Check that amount of members become 3...");
        $(".teams-list__members-count").shouldHave(exactText("3"));
    }

    @Step("Go back to contract AT-227 CNTR and click Share button again")
    public void returnToTheContractAndShareAgain()
    {
        sideBar.clickInProgressContracts(false).selectContract("AT-227 CNTR");

        ShareForm shareForm = new OpenedContract().clickSHARE("AT-14");

        logger.info("Checking that Share form has been opened and participants are in the list...");
        Assert.assertNotNull(shareForm, "Unable to open SHARE form !!!");
        $$(".manage-users-user__fullname").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.textsInAnyOrder("autotest_cn fn ln", "TEAM1"));

        shareForm.disableUser("TEAM1");
        $$(".state_disabled").shouldHave(CollectionCondition.size(1)).first().shouldBe(enabled, visible);

        Screenshoter.makeScreenshot();
        shareForm.clickDone();
    }

    @AfterMethod(description = "Cleanup TEAM1")
    public void removeTeam()
    {
        sideBar.clickAdministration().clickTeamsTab().clickActionMenu("TEAM1").clickDelete().clickDelete();

        logger.info("Assert delete team notification...");
        $(".notification-stack").shouldBe(visible).shouldHave(exactText("Team deleted successfully."));
    }
}

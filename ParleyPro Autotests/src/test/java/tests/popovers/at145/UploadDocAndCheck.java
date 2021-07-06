package tests.popovers.at145;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocAndCheck
{
    @Test
    public void uploadDocAndCheck() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("at-145: check button title");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_DOC_AT141 );

        // Move to Negotiate
        OpenedContract openedContract = new OpenedContract();
        openedContract.switchDocumentToNegotiate("dummyAT141");

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        $(".notification-stack").waitUntil(Condition.appear, 15_000);
        for( int i = 0; i < $$(".notification-stack .notification__close").size(); i++ )
        {
            $(".notification-stack .notification__close").waitUntil(Condition.appear, 5_000).click();
        }

        // Hover
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.contract-header__status .contract-header-users__counterparty span')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        $(".rc-tooltip-inner").waitUntil(Condition.visible, 15_000);
        $(".rc-tooltip-inner button").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("SEND INVITE"));

        // Create queued discussion
        openedContract.clickByParagraph("This is dummy")
                      .setText(" asdf")
                      .selectQueued()
                      .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Queued discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        // Open Manage Discussions
        openedContract.clickManageDiscussions()
                      .makeExternalAllQueuedDiscussions()
                      .confirmMakeExternalForTheFirstTime("at-145: check button title")
                      .clickNext(false)
                      .setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com")
                      .clickStart();
    }
}

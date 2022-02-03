package tests.classic.at203;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import forms.ContractInformation;
import forms.EmailWillBeSentToTheCounterparty;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class SwitchToClassicAndMoveToNegotiate
{
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(SwitchToClassicAndMoveToNegotiate.class);


    @Test(priority = 1)
    @Description("This test switch to Negotiate WITHOUT filled CCN email of ONLINE contract => expect to see 'Counterparty Chief Negotiator can't be blank' error.")
    public void moveToNegotiateWithoutCCNEmail() throws InterruptedException
    {
        openedContract = new OpenedContract();

        logger.info("Move to Negotiate WITHOUT filled CCN email ( ONLINE )...");
        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterparty = openedContract.switchDocumentToNegotiate("Formatting", "", false)
                                                                                          .clickNext(false);

        emailWillBeSentToTheCounterparty.setCounterpartyOrganization("CounterpartyAT")
                                        .clickStart();

        $(".input__error").should(Condition.appear).shouldHave(Condition.exactText("Counterparty Chief Negotiator can't be blank"));
        Screenshoter.makeScreenshot();

        emailWillBeSentToTheCounterparty.clickCancel();
    }

    @Test(priority = 2)
    @Description("This test switch to Negotiate WITHOUT filled CCN email of CLASSIC contract => switch should be successful.")
    public void switchToClassicAndMoveToNegotiate() throws InterruptedException
    {
        ContractInformation contractInfo = openedContract.clickContractInfo();

        logger.info("Switching contract to CLASSIC...");
        contractInfo.checkClassicNegotiationModeCheckbox();
        Thread.sleep(1_000);
        contractInfo.clickSave();


        logger.info("Move to Negotiate WITHOUT filled CCN email ( CLASSIC )...");
        Thread.sleep(2_000);
        openedContract.switchDocumentToNegotiate("Formatting", "", true)
                      .clickNext(true)
                      .setCounterpartyOrganization("CounterpartyAT")
                      .clickStart();

        logger.info("Making sure that status was changed to NEGOTIATE...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
        Screenshoter.makeScreenshot();
    }
}

package tests.document_sharing;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ShareForm;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

public class CancelingOfAddedUsers
{
    private static Logger logger = Logger.getLogger(CancelingOfAddedUsers.class);

    @Test
    @Description("This test repeats scenario from AT-55")
    public void cancelingOfAddedUsers() throws InterruptedException
    {
        ShareForm shareForm = new OpenedContract().clickSHARE("Formatting");

        shareForm.addParticipant( Const.USER_FELIX.getFirstName() );
        shareForm.removeUser( Const.USER_FELIX.getFirstName() );

        shareForm.addParticipant( Const.USER_FELIX.getFirstName() );
        shareForm.addParticipant( Const.USER_MARY.getFirstName() );

        shareForm.removeUser( Const.USER_FELIX.getFirstName() );
        shareForm.removeUser( Const.USER_MARY.getFirstName() );

        logger.info("Checking that there are no added users left in share modal...");
        $$(".manage-users-user").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("AL\nautotest_cn fn ln\narthur.khasanov+autotestcn@parleypro.com\nChief Negotiator"));

        Screenshoter.makeScreenshot();

        shareForm.clickDone();
    }
}

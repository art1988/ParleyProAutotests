package tests.document_sharing;

import constants.Const;
import forms.ShareForm;
import org.testng.annotations.Test;
import pages.OpenedContract;

public class SharingFunctionality
{
    @Test
    public void sharingFunctionality() throws InterruptedException
    {
        ShareForm shareForm = new OpenedContract().clickSHARE("Formatting");

        shareForm.addParticipant( Const.PREDEFINED_INTERNAL_USER_1.getFirstName() ).clickSend();

        Thread.sleep(50_000 );
    }
}

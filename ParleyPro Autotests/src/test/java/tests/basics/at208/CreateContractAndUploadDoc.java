package tests.basics.at208;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInformation;
import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndUploadDoc
{
    @Test
    public void createContractAndUploadDoc()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(false).clickNewContractButton();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withLocale(Locale.US);
        String dynamicContractTitle = "AT-208_discussions_against_TrackChanges_" + LocalDateTime.now().format(formatter);
        Cache.getInstance().setContractTitle(dynamicContractTitle);

        contractInformationForm.setContractTitle( dynamicContractTitle );
        contractInformationForm.clickSave();


        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        new OpenedContract().switchDocumentToReview(FilenameUtils.removeExtension(Const.DOCUMENT_DISCUSSIONS_SAMPLE.getName())).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
    }
}

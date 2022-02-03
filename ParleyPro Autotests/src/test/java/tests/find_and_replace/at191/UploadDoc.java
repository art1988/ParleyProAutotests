package tests.find_and_replace.at191;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Cache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class UploadDoc
{
    @Test
    public void createContractAndUploadDoc()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformation = new InProgressContractsPage(false).clickNewContractButton();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withLocale(Locale.US);
        String dynamicContractTitle = "CNTR AT-191: " + LocalDateTime.now().format(formatter);

        Cache.getInstance().setContractTitle(dynamicContractTitle);

        contractInformation.setContractTitle( dynamicContractTitle );
        contractInformation.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOC_AT166_ONE );

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("has been successfully uploaded."));

        // Move to Review
        new OpenedContract().switchDocumentToReview("AT-166_Manufacturing Agreement_1").clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
    }
}

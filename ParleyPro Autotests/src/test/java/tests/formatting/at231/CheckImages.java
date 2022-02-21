package tests.formatting.at231;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckImages
{
    private static Logger logger = Logger.getLogger(CheckImages.class);


    @Test
    public void checkImages()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-231 // Image disappearing");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOC_AT231_IMG_DISAPPEARING );

        new OpenedContract();
        new ContractInNegotiation("AT-231 // Image disappearing").clickOk();

        logger.info("Assert that both images are present...");
        $$("img").shouldHave(CollectionCondition.size(2)).forEach(img -> Assert.assertTrue(img.isImage()));
        $$(".document-paragraph__content span").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.textsInAnyOrder("Test images disappearing", "This text was added"));
        Screenshoter.makeScreenshot();
    }
}

package tests.formatting.at160;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocWithTableAndCheck
{
    private static Logger logger = Logger.getLogger(UploadDocWithTableAndCheck.class);

    @Test
    public void uploadDocWithTableAndCheck()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-160 // Upload document with table");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.DOCUMENT_TABLE_SAMPLE );

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.text("has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 45_000);

        logger.info("Checking integrity of table...");
        $$("table[class='keep-border-top']").shouldHave(CollectionCondition.size(1)); // Only 1 table should be on the page
        $("table[class='keep-border-top']").should(Condition.exist); // start from .keep-border-top -> first row

        WebElement secondRow = Selenide.executeJavaScript("return $('table[class=\"keep-border-top\"]').parent().parent().parent().parent().next().find(\"table\")[0]");
        $(secondRow).should(Condition.exist); // 2nd row of table

        WebElement thirdRow = Selenide.executeJavaScript("return $('table[class=\"keep-border-top\"]').parent().parent().parent().parent().next().next().find(\"table\")[0]");
        $(thirdRow).should(Condition.exist); // 3rd row of table

        Assert.assertEquals((long) Selenide.executeJavaScript("return $('table[class=\"keep-border-top\"]').parent().parent().parent().parent().next().next().next().find(\"table\").length"), 0l); // end of table. No 4th row

        logger.info("Checking cells of table...");
        $$("table[class='keep-border-top'] td").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("Cell 11", "Cell 12", "Cell 13"));
        $$(secondRow.findElements(By.cssSelector("td"))).shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("Cell 21", "Cell 22", "Cell 23"));
        $$(thirdRow.findElements(By.cssSelector("td"))).shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("Cell 31", "Cell 32", "Cell 33"));

        Screenshoter.makeScreenshot();
    }
}

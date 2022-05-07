package tests.fields.at116;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class DeletePredefinedFields
{
    private static Logger logger = Logger.getLogger(DeletePredefinedFields.class);

    @Test
    @Description("This test deletes all predefined fields on Fields page and checks deletion.")
    public void deleteAllPredefinedFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar()
                                              .clickAdministration()
                                              .clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        logger.info("Check that all predefined fields are in place...");
        $$(".input__label-required").shouldHave(CollectionCondition.size(6));

        logger.info("Removing of all predefined fields...");
        contractFields.removeField("Contracting region").clickDelete();
        contractFields.removeField("Contracting country").clickDelete();
        contractFields.removeField("Contract entity").clickDelete();
        contractFields.removeField("Contracting department").clickDelete();
        contractFields.removeField("Contract category").clickDelete();
        contractFields.removeField("Contract type").clickDelete();

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Check that all predefined fields were deleted...");
        Assert.assertEquals($$(".input__label-required").size(), 0, "There are still some required fields remained but shouldn't !!!");

        Screenshoter.makeScreenshot();
    }
}

package tests.fields.at116;

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
public class RestoreFields
{
    private static Logger logger = Logger.getLogger(RestoreFields.class);

    @Test
    @Description("This test restores all predefined fields that are needed for future tests.")
    public void restoreFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar()
                                              .clickAdministration()
                                              .clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.addPredefinedFields().selectField("Contracting region")
                                            .selectField("Contracting country")
                                            .selectField("Contract entity")
                                            .selectField("Contracting department")
                                            .selectField("Contract category")
                                            .selectField("Contract type")
                                            .clickAdd();

        logger.info("Assert that all required fields were added...");
        Assert.assertEquals($$(".input__label-required").size(), 6, "Number of required fields is mismatched !!!");

        // Fill up with values...
        contractFields.addValues("Contracting region", "region1");
        contractFields.addValues("Contracting region", "region2");
        contractFields.addValues("Contracting region", "region3");
        contractFields.resetValueIndex();

        contractFields.addValues("Contracting country", "country1");
        contractFields.addValues("Contracting country", "country2");
        contractFields.resetValueIndex();

        contractFields.addValues("Contract entity", "entity1");
        contractFields.addValues("Contract entity", "entity2");
        contractFields.resetValueIndex();

        contractFields.addValues("Contracting department", "department1");
        contractFields.addValues("Contracting department", "department2");
        contractFields.resetValueIndex();

        contractFields.addValues("Contract category", "category1");
        contractFields.addValues("Contract category", "category2");
        contractFields.resetValueIndex();

        contractFields.addValues("Contract type", "type1");
        contractFields.addValues("Contract type", "type2");
        contractFields.addValues("Contract type", "type3");
        contractFields.resetValueIndex();

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Assert that values were added...");
        Assert.assertEquals($$(".input .input__input.form-control").size(), 6, "Looks like that some values weren't set !!!");

        Screenshoter.makeScreenshot();
    }
}

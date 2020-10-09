package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

public class ContractInformation
{
    private SelenideElement contractTitleField         = $("input[inputid='contractTitle']");
    private SelenideElement contractingRegionField     = $("#contractingRegion");
    private SelenideElement contractingCountryField    = $("#contractingCountry");
    private SelenideElement contractEntityField        = $("#contractEntity");
    private SelenideElement contractingDepartmentField = $("#ContractingDepartment");
    private SelenideElement contractCategoryField      = $("#contractCategory");


    private static Logger logger = Logger.getLogger(ContractInformation.class);

    public ContractInformation()
    {
        Assert.assertTrue(isInit());
    }

    private boolean isInit()
    {
        $(".contract-create__title").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("Contract information"));

        return ( contractTitleField.isDisplayed() );
    }

    public void setContractTitle(String title)
    {
        contractTitleField.clear();
        contractTitleField.setValue(title);
    }

    public void setContractingRegion(String region)
    {
        contractingRegionField.clear();
        contractingRegionField.setValue(region);
    }

    public void setContractingCountry(String country)
    {
        contractingCountryField.clear();
        contractingCountryField.setValue(country);
    }

    public void setContractEntity(String entity)
    {
        contractEntityField.clear();
        contractEntityField.setValue(entity);
    }

    public void setContractingDepartment(String department)
    {
        contractingDepartmentField.clear();
        contractingDepartmentField.setValue(department);
    }

    public void setContractCategory(String category)
    {
        contractCategoryField.clear();
        contractCategoryField.setValue(category);
    }

    public void setContractType(String type)
    {
        Selenide.executeJavaScript("$('input[inputid=\"contractType\"]').click()");
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
    }

    public String getChiefNegotiator()
    {
        String chiefNegotiator = Selenide.executeJavaScript("return $('#ChiefNegotiator').val()");

        return chiefNegotiator;
    }

    public void clickSave()
    {
        // wait until SAVE button is enabled
        $(".button.btn.btn-common.btn-blue.btn.btn-default").waitUntil(Condition.enabled, 5_000);

        Selenide.executeJavaScript("$('.pull-right button:contains(\"SAVE\")').click()");

        logger.info("Save button was clicked");
    }
}

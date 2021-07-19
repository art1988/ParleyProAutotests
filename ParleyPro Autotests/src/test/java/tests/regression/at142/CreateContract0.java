package tests.regression.at142;

import com.codeborne.selenide.Condition;
import forms.ContractInformation;
import forms.add.AddPredefinedFields;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContract0
{
    @Test(priority = 1)
    @Description("Creates 'Contract 0' with with empty dep")
    public void createContract0()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Contract 0");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();
    }

    @Test(priority = 2)
    @Description("Enables Contracting Department field back and adds 2 departments")
    public void enableContractingDepartmentField()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        AddPredefinedFields addPredefinedFieldsForm = contractFields.addPredefinedFields();
        addPredefinedFieldsForm.selectField("Contracting department");
        addPredefinedFieldsForm.clickAdd();

        contractFields.addValues("Contracting department", "Dep1");
        contractFields.addValues("Contracting department", "Dep2");

        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}

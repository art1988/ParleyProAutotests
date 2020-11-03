package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ApprovalWorkflow
{
    private SelenideElement nameField       = $("#workflows-approval-name");
    private SelenideElement categoryField   = $("input[data-id='workflows-approval-cate']");
    private SelenideElement typeField       = $("input[data-id='workflows-approval-type']");
    private SelenideElement departmentField = $("input[data-id='workflows-approval-department']");



    public ApprovalWorkflow()
    {
        $(".workflows__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Approval workflow"));
    }

    public void setName(String workflowName)
    {
        nameField.setValue(workflowName);
    }

    public void setCategory(String category)
    {
        categoryField.setValue(category);
    }

    public void setType(String type)
    {
        typeField.setValue(type);
    }

    public void setDepartment(String department)
    {
        departmentField.setValue(department);
    }
}
